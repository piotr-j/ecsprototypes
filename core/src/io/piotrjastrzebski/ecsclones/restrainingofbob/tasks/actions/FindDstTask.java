package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.actions;

import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import com.badlogic.gdx.math.Vector2;
import io.piotrjastrzebski.bteditor.core.TaskComment;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.CircleBounds;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai.storage.IntStorage;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.base.BaseTask;

/**
 * Created by PiotrJ on 19/08/15.
 */
@Wire(injectInherited = true)
public class FindDstTask extends BaseTask implements TaskComment {
	private final static String TAG = FindDstTask.class.getSimpleName();

	@TaskAttribute(required=true)
	public String target;

	@TaskAttribute(required=true)
	public String dstVar;

	protected ComponentMapper<CircleBounds> mCircleBounds;

	private static Vector2 tmp = new Vector2();
	@Override public Status execute() {
		EnemyBrain brain = getObject();
		IntStorage storage = brain.getIntStorage();
		if (!storage.hasValue(target)) return Status.FAILED;
		int otherID = storage.getValue(target);
		CircleBounds srcCB = mCircleBounds.getSafe(brain.id);
		CircleBounds tarCB = mCircleBounds.getSafe(otherID);
		if (srcCB == null || tarCB == null) return Status.FAILED;
		float dst = tmp.set(srcCB.bounds.x, srcCB.bounds.y).dst(tarCB.bounds.x, tarCB.bounds.y);
		brain.getFloatStorage().putValue(dstVar, dst);
		return Status.SUCCEEDED;
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		FindDstTask range = (FindDstTask)task;
		range.target = target;
		range.dstVar = dstVar;
		range.mCircleBounds = mCircleBounds;
		return super.copyTo(task);
	}

	@Override public String getComment () {
		return "Check dstOuter between 2 entities, IN/OUT within/outside of range, outer > inner";
	}
}
