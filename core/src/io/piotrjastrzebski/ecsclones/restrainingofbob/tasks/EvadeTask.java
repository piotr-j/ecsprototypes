package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.BEvader;

/**
 * Created by PiotrJ on 19/08/15.
 */
@Wire(injectInherited = true)
public class EvadeTask extends BaseTask {
	private final static String TAG = EvadeTask.class.getSimpleName();

	@TaskAttribute(required=true)
	public String target;

	BEvader evader;

	@Override public void start () {
		Gdx.app.log(TAG, "start!");
	}

	@Override public void run () {
		EnemyBrain brain = getObject();
		// fail if dead or player our of range
		if (evader.set(brain.id, target)) {
			running();
		} else {
			fail();
		}
//		if (evader.set(brain.id, target)) {
//			running();
//		} else {
//			fail();
//		}
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		EvadeTask range = (EvadeTask)task;
		range.target = target;
		range.evader = evader;
		return super.copyTo(task);
	}
}
