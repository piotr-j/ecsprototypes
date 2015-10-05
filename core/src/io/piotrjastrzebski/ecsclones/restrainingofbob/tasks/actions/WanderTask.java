package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.actions;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.ai.btree.Task;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai.BWanderer;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.base.BaseTask;

/**
 * Created by PiotrJ on 19/08/15.
 */
@Wire(injectInherited = true)
public class WanderTask extends BaseTask {
	private final static String TAG = WanderTask.class.getSimpleName();
	BWanderer wanderer;
	@Override public void start () {
		wanderer.set(getObject().id);
	}

	@Override public void run () {
		running();
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		WanderTask wander = (WanderTask)task;
		wander.wanderer = wanderer;
		return super.copyTo(task);
	}
}
