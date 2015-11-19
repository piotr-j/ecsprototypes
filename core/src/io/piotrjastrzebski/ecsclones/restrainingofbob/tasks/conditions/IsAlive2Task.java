package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.conditions;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import io.piotrjastrzebski.bteditor.core.TaskComment;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.status.Dead;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai.storage.IntStorage;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.base.BaseTask;

/**
 * Created by PiotrJ on 19/08/15.
 */
@Wire(injectInherited = true)
public class IsAlive2Task extends BaseTask implements TaskComment {
	private final static String TAG = IsAlive2Task.class.getSimpleName();

	@TaskAttribute(required=true)
	public String entityIdVar;

	protected ComponentMapper<Dead> mDead;

	@Override public Status execute() {
		EnemyBrain brain = getObject();
		IntStorage ints = brain.getIntStorage();
		if (!ints.hasValue(entityIdVar)) return Status.FAILED;
		int entityId = ints.getValue(entityIdVar);
		return mDead.has(entityId) ? Status.FAILED : Status.SUCCEEDED;
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		IsAlive2Task range = (IsAlive2Task)task;
		range.entityIdVar = entityIdVar;
		range.mDead = mDead;
		return super.copyTo(task);
	}

	@Override public String getComment () {
		return "Check if entity with id stored at given stack is alive";
	}
}
