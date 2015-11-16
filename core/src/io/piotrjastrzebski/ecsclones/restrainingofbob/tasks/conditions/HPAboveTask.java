package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.conditions;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.base.BaseTask;

/**
 * Created by PiotrJ on 19/08/15.
 */
@Wire(injectInherited = true)
public class HPAboveTask extends BaseTask {
	private final static String TAG = HPAboveTask.class.getSimpleName();

	@TaskAttribute
	public float percent;

	@TaskAttribute
	public float absolute;

	@Override public Status execute() {
		EnemyBrain brain = getObject();
		float test = 0;
		if (percent > 0) {
			test = brain.maxHP * percent * 0.01f;
		} else if (absolute > 0) {
			test = absolute;
		}
		if (brain.hp > test) {
			return Status.SUCCEEDED;
		} else {
			return Status.FAILED;
		}
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		HPAboveTask hp = (HPAboveTask)task;
		hp.percent = percent;
		hp.absolute = absolute;
		return super.copyTo(task);
	}
}
