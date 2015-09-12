package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.btree.Task;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.BWanderer;

/**
 * Created by PiotrJ on 19/08/15.
 */
@Wire
public class WanderTask extends BaseTask {
	private final static String TAG = WanderTask.class.getSimpleName();
	BWanderer wanderer;
	@Override public void start () {
		wanderer.set(getObject().id);
	}

	@Override public void run () {
		success();
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		WanderTask wander = (WanderTask)task;
		wander.wanderer = wanderer;
		return super.copyTo(task);
	}
}
