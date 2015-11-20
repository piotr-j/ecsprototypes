package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.conditions;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.Finder;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai.storage.IntStorage;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.base.BaseTask;

/**
 * Created by PiotrJ on 19/08/15.
 */
@Wire(injectInherited = true)
public class DstCheckTask extends BaseTask {
	private final static String TAG = DstCheckTask.class.getSimpleName();
	enum CheckType {GT, LT, IN, OUT}
	@TaskAttribute(required=true)
	public String idName;

	@TaskAttribute(required=true)
	public CheckType type = CheckType.GT;

	@TaskAttribute(required=true)
	public float dst;

	Finder finder;

	@Override public Status execute() {
		EnemyBrain brain = getObject();
		IntStorage storage = brain.getIntStorage();
		if (!storage.hasValue(idName)) return Status.FAILED;
		// TODO implement proper range check
		if (finder.overlaps(brain.id, storage.getValue(idName), dst)) {
			return Status.SUCCEEDED;
		} else {
			return Status.FAILED;
		}
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		DstCheckTask range = (DstCheckTask)task;
		range.idName = idName;
		range.finder = finder;
		range.dst = dst;
		return super.copyTo(task);
	}
}
