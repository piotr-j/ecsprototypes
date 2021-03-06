package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.conditions;

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
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.base.ComparisonType;

/**
 * Created by PiotrJ on 19/08/15.
 */
@Wire(injectInherited = true)
public class DstCheckTask extends BaseTask implements TaskComment {
	private final static String TAG = DstCheckTask.class.getSimpleName();

	@TaskAttribute(required=true)
	public String idName;

	@TaskAttribute(required=true)
	public ComparisonType type = ComparisonType.GT;

	@TaskAttribute(required=true)
	public float dstOuter;

	@TaskAttribute
	public float dstInner;

	protected ComponentMapper<CircleBounds> mCircleBounds;

	private static Vector2 tmp = new Vector2();
	@Override public Status execute() {
		EnemyBrain brain = getObject();
		IntStorage storage = brain.getIntStorage();
		if (!storage.hasValue(idName)) return Status.FAILED;
		int otherID = storage.getValue(idName);
		CircleBounds srcCB = mCircleBounds.getSafe(brain.id);
		CircleBounds tarCB = mCircleBounds.getSafe(otherID);
		if (srcCB == null || tarCB == null) return Status.FAILED;
		float dst2 = tmp.set(srcCB.bounds.x, srcCB.bounds.y).dst2(tarCB.bounds.x, tarCB.bounds.y);

		return type.compare(dst2, dstOuter * dstOuter)? Status.SUCCEEDED : Status.FAILED;
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		DstCheckTask range = (DstCheckTask)task;
		range.idName = idName;
		range.type = type;
		range.dstOuter = dstOuter;
		range.dstInner = dstInner;
		range.mCircleBounds = mCircleBounds;
		return super.copyTo(task);
	}

	@Override public String getComment () {
		return "Check dstOuter between 2 entities, IN/OUT within/outside of range, outer > inner";
	}
}
