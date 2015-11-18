package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.stacks;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai.storage.FloatStorage;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai.storage.IntStorage;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai.storage.StringStorage;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.base.BaseTask;

/**
 * Created by PiotrJ on 19/08/15.
 */
@Wire(injectInherited = true)
public class StackPopTask extends BaseTask {
	private final static String TAG = StackPopTask.class.getSimpleName();

	@TaskAttribute(required=true)
	public String name;

	@TaskAttribute(required=true)
	public ValueType type = ValueType.INT;

	@Override public Status execute() {
		EnemyBrain eb = getObject();
		switch (type) {
		case INT:
			IntStorage intStorage = eb.getIntStorage();
			if (intStorage.isStackEmpty(name)) return Status.FAILED;
			intStorage.popFromStack(name);
			return Status.SUCCEEDED;
		case FLOAT:
			FloatStorage floatStorage = eb.getFloatStorage();
			if (floatStorage.isStackEmpty(name)) return Status.FAILED;
			floatStorage.popFromStack(name);
			return Status.SUCCEEDED;
		case STRING:
			StringStorage stringStorage = eb.getStringStorage();
			if (stringStorage.isStackEmpty(name)) return Status.FAILED;
			stringStorage.popFromStack(name);
			return Status.SUCCEEDED;
		}
		return Status.FAILED;
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		StackPopTask range = (StackPopTask)task;
		range.name = name;
		range.type = type;
		return super.copyTo(task);
	}
}
