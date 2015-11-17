package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.actions;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai.MonsterShooter;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.base.BaseTask;

/**
 * Created by PiotrJ on 19/08/15.
 */
@Wire(injectInherited = true)
public class ShootTask extends BaseTask {
	private final static String TAG = ShootTask.class.getSimpleName();

	MonsterShooter shooter;

	@TaskAttribute(required=true)
	public String target;

	@Override public Status execute() {
		EnemyBrain brain = getObject();
		if (shooter.attack(brain.id, target)) {
			return Status.SUCCEEDED;
		} else {
			return Status.FAILED;
		}
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		ShootTask melee = (ShootTask)task;
		melee.target = target;
		melee.shooter = shooter;
		return super.copyTo(task);
	}
}
