package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.actions;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai.BEvader;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.base.BaseTask;

/**
 * Created by PiotrJ on 19/08/15.
 */
@Wire(injectInherited = true)
public class Evade2Task extends BaseTask {
	private final static String TAG = Evade2Task.class.getSimpleName();

	@TaskAttribute(required=true)
	public String target;

	BEvader evader;

	@Override public Status execute() {
		EnemyBrain brain = getObject();
		// fail if dead or player our of range
		if (evader.set(brain.id, brain.getIntStorage().getValue(target))) {
			return Status.RUNNING;
		} else {
			return Status.FAILED;
		}
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		Evade2Task range = (Evade2Task)task;
		range.target = target;
		range.evader = evader;
		return super.copyTo(task);
	}
}
