package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.actions;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai.BPursuer;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.base.BaseTask;

/**
 * Created by PiotrJ on 19/08/15.
 */
@Wire(injectInherited = true)
public class PursueTask extends BaseTask {
	private final static String TAG = PursueTask.class.getSimpleName();

	@TaskAttribute(required=true)
	public String target;

	BPursuer pursuer;

	@Override public void start () {
		EnemyBrain brain = getObject();
		if (pursuer.set(brain.id, target)) {
			running();
		} else {
			fail();
		}
	}


	@Override public void run () {
		running();
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		PursueTask range = (PursueTask)task;
		range.target = target;
		range.pursuer = pursuer;
		return super.copyTo(task);
	}
}
