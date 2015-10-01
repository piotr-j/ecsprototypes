package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.base.BaseDecorator;

/**
 * Created by PiotrJ on 19/08/15.
 */
@Wire(injectInherited = true)
public class LimitDecorator extends BaseDecorator {
	private final static String TAG = LimitDecorator.class.getSimpleName();

	@TaskAttribute(required=true)
	public float delay;

	public float timer;

	@Override public void start () {
		if (timer <= 0) {
			super.start();
		}
	}

	@Override public void run () {
		if (timer <= 0) {
			super.run();
		} else {
			// this is potentially broken if run doesnt run each tick, must be in parallel
			timer -= world.delta;
			fail();
		}
	}

	@Override public void childSuccess (Task<EnemyBrain> runningTask) {
		super.childSuccess(runningTask);
		timer = delay;
		super.end();
	}

	@Override public void end () {
		if (timer <= 0) {
			super.end();
		}
	}

	@Override
	protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		super.copyTo(task);
		LimitDecorator ld = (LimitDecorator)task;
		ld.delay = delay;
		return task;
	}
}
