package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.BPursuer;

/**
 * Created by PiotrJ on 19/08/15.
 */
@Wire
public class PursueTask extends BaseTask {
	private final static String TAG = PursueTask.class.getSimpleName();

	@TaskAttribute(required=true)
	public String target;

	BPursuer pursuer;

	@Override public void start () {
		EnemyBrain brain = getObject();
		Gdx.app.log(TAG, "start!");
		if (pursuer.set(brain.id, target)) {
			running();
		} else {
			fail();
		}
	}


	@Override public void run () {
		if (getObject().inRange) {
			running();
		} else {
			fail();
		}
	}

	@Override public void end () {
		Gdx.app.log(TAG, "end!");
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		PursueTask range = (PursueTask)task;
		range.target = target;
		range.pursuer = pursuer;
		return super.copyTo(task);
	}
}
