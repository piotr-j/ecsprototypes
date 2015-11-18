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
public class StackPushTask extends BaseTask {
	private final static String TAG = StackPushTask.class.getSimpleName();

	@TaskAttribute(required=true)
	public String name;

	@TaskAttribute(required=true)
	public ValueType type = ValueType.INT;

	@Override public Status execute() {
		EnemyBrain eb = getObject();
		switch (type) {
		case INT:
			IntStorage intStorage = eb.getIntStorage();
//			intStorage.pushTo(name, eb.intValue);
			return Status.SUCCEEDED;
		case FLOAT:
			FloatStorage floatStorage = eb.getFloatStorage();
//			floatStorage.pushTo(name, eb.floatValue);
			return Status.SUCCEEDED;
		case STRING:
			StringStorage stringStorage = eb.getStringStorage();
//			stringStorage.pushTo(name, eb.stringValue);
			return Status.SUCCEEDED;
		}
		return Status.FAILED;
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		StackPushTask range = (StackPushTask)task;
		range.name = name;
		range.type = type;
		return super.copyTo(task);
	}
}
