package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.BPursuer;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.Meleer;

/**
 * Created by PiotrJ on 19/08/15.
 */
@Wire
public class MeleeTask extends BaseTask {
	private final static String TAG = MeleeTask.class.getSimpleName();

	Meleer meleer;

	@TaskAttribute(required=true)
	public String target;

	@Override public void start () {
		EnemyBrain brain = getObject();
		if (meleer.attack(brain.id, target)) {
			success();
		} else {
			fail();
		}
	}


	@Override public void run () {
		running();
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		MeleeTask melee = (MeleeTask)task;
		melee.target = target;
		melee.meleer = meleer;
		return super.copyTo(task);
	}
}
