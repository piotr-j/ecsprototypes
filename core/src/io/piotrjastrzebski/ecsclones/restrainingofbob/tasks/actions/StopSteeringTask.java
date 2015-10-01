package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.actions;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.ai.btree.Task;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai.BSteeringStopper;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.base.BaseTask;

/**
 * Created by PiotrJ on 19/08/15.
 */
@Wire(injectInherited = true)
public class StopSteeringTask extends BaseTask {
	private final static String TAG = StopSteeringTask.class.getSimpleName();

	@Wire BSteeringStopper stopper;

	@Override public void start () {
		EnemyBrain brain = getObject();
		if (stopper.set(brain.id)) {
			success();
		} else {
			fail();
		}
	}

	@Override public void run () {
		running();
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		StopSteeringTask stop = (StopSteeringTask)task;
		stop.stopper = this.stopper;
		return super.copyTo(task);
	}
}
