package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.actions;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai.MonsterAttack;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai.MonsterMelee;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.base.BaseTask;

/**
 * Created by PiotrJ on 19/08/15.
 */
@Wire(injectInherited = true)
public class AttackTask extends BaseTask {
	private final static String TAG = AttackTask.class.getSimpleName();

	@Wire
	MonsterAttack attacker;

	@TaskAttribute(required=true)
	public String target;

	@Override public Status execute() {
		EnemyBrain brain = getObject();
		if (attacker.attack(brain.id, target)) {
			return Status.SUCCEEDED;
		} else {
			return Status.FAILED;
		}
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		AttackTask melee = (AttackTask)task;
		melee.target = target;
		melee.attacker = attacker;
		return super.copyTo(task);
	}
}
