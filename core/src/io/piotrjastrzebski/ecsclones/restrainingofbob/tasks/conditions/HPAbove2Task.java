package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.conditions;

import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import io.piotrjastrzebski.bteditor.core.TaskComment;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.status.Health;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai.storage.IntStorage;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.base.BaseTask;

/**
 * Created by PiotrJ on 19/08/15.
 */
@Wire(injectInherited = true)
public class HPAbove2Task extends BaseTask implements TaskComment {
	private final static String TAG = HPAbove2Task.class.getSimpleName();

	@TaskAttribute(required=true)
	public String idName;

	@TaskAttribute
	public float percent;

	@TaskAttribute
	public float absolute;

	ComponentMapper<Health> mHealth;

	@Override public Status execute() {
		EnemyBrain brain = getObject();
		IntStorage storage = brain.getIntStorage();
		if (!storage.hasValue(idName)) return Status.FAILED;
		int eid = storage.getValue(idName);
		Health health = mHealth.getSafe(eid, null);
		if (health == null) return Status.FAILED;

		float test = 0;
		if (percent > 0) {
			test = health.maxHp * percent * 0.01f;
		} else if (absolute > 0) {
			test = absolute;
		}
		if (health.hp > test) {
			return Status.SUCCEEDED;
		} else {
			return Status.FAILED;
		}
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		HPAbove2Task hp = (HPAbove2Task)task;
		hp.idName = idName;
		hp.percent = percent;
		hp.absolute = absolute;
		return super.copyTo(task);
	}

	@Override public String getComment () {
		return "Check if health of stored entity is above certain threshold";
	}
}
