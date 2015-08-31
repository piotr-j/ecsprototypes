package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.EnemyBrain;

/**
 * Created by PiotrJ on 19/08/15.
 */
@Wire
public class HPAboveTask extends BaseTask {
	private final static String TAG = HPAboveTask.class.getSimpleName();

	@TaskAttribute
	public float percent;

	@TaskAttribute
	public float absolute;

	@Override public void run () {
		EnemyBrain brain = getObject();
		float test = 0;
		if (percent > 0) {
			test = brain.maxHP * percent * 0.01f;
		} else if (absolute > 0) {
			test = absolute;
		}
		if (brain.hp > test) {
//			Gdx.app.log(TAG, "hp high!" + brain.hp);
			success();
		} else {
//			Gdx.app.log(TAG, "hp low! " + brain.hp);
			fail();
		}
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		HPAboveTask hp = (HPAboveTask)task;
		hp.percent = percent;
		hp.absolute = absolute;
		return super.copyTo(task);
	}
}
