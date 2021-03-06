package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.conditions;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.Finder;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.base.BaseTask;

/**
 * Created by PiotrJ on 19/08/15.
 */
@Wire(injectInherited = true)
public class InRangeTask extends BaseTask {
	private final static String TAG = InRangeTask.class.getSimpleName();

	@TaskAttribute(required=true)
	public String target;

	@TaskAttribute(required=true)
	public float dst;

	Finder finder;

	@Override public Status execute() {
		EnemyBrain brain = getObject();
		if (finder.overlaps(brain.id, target, dst)) {
			return Status.SUCCEEDED;
		} else {
			return Status.FAILED;
		}
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		InRangeTask range = (InRangeTask)task;
		range.target = target;
		range.finder = finder;
		range.dst = dst;
		return super.copyTo(task);
	}
}
