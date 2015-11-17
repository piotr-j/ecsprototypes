package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.conditions;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.status.Dead;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.base.BaseTask;

/**
 * Created by PiotrJ on 19/08/15.
 */
@Wire(injectInherited = true)
public class IsAliveTask extends BaseTask {
	private final static String TAG = IsAliveTask.class.getSimpleName();

	@TaskAttribute(required=true)
	public String target;

	TagManager tags;
	protected ComponentMapper<Dead> mDead;

	@Override public Status execute() {
		Entity te = tags.getEntity(target);
		if (te == null || mDead.has(te)) {
			return Status.FAILED;
		} else {
			return Status.SUCCEEDED;
		}
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		IsAliveTask range = (IsAliveTask)task;
		range.target = target;
		range.tags = tags;
		range.mDead = mDead;
		return super.copyTo(task);
	}
}
