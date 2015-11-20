package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.conditions;

import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import com.badlogic.gdx.math.MathUtils;
import io.piotrjastrzebski.bteditor.core.TaskComment;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.status.Health;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai.storage.IntStorage;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.base.BaseTask;

/**
 * Created by PiotrJ on 19/08/15.
 */
@Wire(injectInherited = true)
public class HPCheckTask extends BaseTask implements TaskComment {
	enum CHECK {GT, GT_EQ, LT, LT_EQ, EQ, NEQ}
	private final static String TAG = HPCheckTask.class.getSimpleName();

	@TaskAttribute(required=true)
	public String idName;

	@TaskAttribute(required=true)
	public CHECK type;

	@TaskAttribute(required=true)
	public float percent;

	ComponentMapper<Health> mHealth;

	@Override public Status execute() {
		EnemyBrain brain = getObject();
		IntStorage storage = brain.getIntStorage();
		if (!storage.hasValue(idName)) return Status.FAILED;
		int eid = storage.getValue(idName);
		Health health = mHealth.getSafe(eid, null);
		if (health == null) return Status.FAILED;

		float test = health.maxHp * percent * 0.01f;
		boolean eq = MathUtils.isEqual(health.hp, test);
		switch (type) {
		case GT:
			return (health.hp > test)?Status.SUCCEEDED:Status.FAILED;
		case GT_EQ:
			return (health.hp > test || eq)?Status.SUCCEEDED:Status.FAILED;
		case LT:
			return (health.hp < test)?Status.SUCCEEDED:Status.FAILED;
		case LT_EQ:
			return (health.hp > test || eq)?Status.SUCCEEDED:Status.FAILED;
		case EQ:
			return eq?Status.SUCCEEDED:Status.FAILED;
		case NEQ:
			return eq?Status.FAILED:Status.SUCCEEDED;
		}
		return Status.FAILED;
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		HPCheckTask hp = (HPCheckTask)task;
		hp.idName = idName;
		hp.percent = percent;
		hp.type = type;
		hp.mHealth = mHealth;
		return super.copyTo(task);
	}

	@Override public String getComment () {
		return "Check if health of stored entity is above certain threshold";
	}
}
