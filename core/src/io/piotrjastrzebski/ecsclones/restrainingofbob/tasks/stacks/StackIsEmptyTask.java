package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.stacks;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.base.BaseTask;

/**
 * Created by PiotrJ on 19/08/15.
 */
@Wire(injectInherited = true)
public class StackIsEmptyTask extends BaseTask {
	private final static String TAG = StackIsEmptyTask.class.getSimpleName();

	@TaskAttribute(required=true)
	public String name;

	@TaskAttribute(required=true)
	public ValueType type = ValueType.INT;

	@Override public Status execute() {
		EnemyBrain eb = getObject();
		switch (type) {
		case INT:
			return eb.getIntStorage().isStackEmpty(name)?Status.SUCCEEDED:Status.FAILED;
		case FLOAT:
			return eb.getFloatStorage().isStackEmpty(name)?Status.SUCCEEDED:Status.FAILED;
		case STRING:
			return eb.getStringStorage().isStackEmpty(name)?Status.SUCCEEDED:Status.FAILED;
		}
		return Status.FAILED;
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		StackIsEmptyTask range = (StackIsEmptyTask)task;
		range.name = name;
		range.type = type;
		return super.copyTo(task);
	}
}
