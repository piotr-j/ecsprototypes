package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.stacks;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import io.piotrjastrzebski.bteditor.core.TaskComment;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai.storage.FloatStorage;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai.storage.IntStorage;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai.storage.StringStorage;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.base.BaseTask;

/**
 * Created by PiotrJ on 19/08/15.
 */
@Wire(injectInherited = true)
public class StackPush extends BaseTask implements TaskComment {
	private final static String TAG = StackPush.class.getSimpleName();

	@TaskAttribute(required=true)
	public String varName;

	@TaskAttribute(required=true)
	public ValueType type = ValueType.INT;

	@TaskAttribute(required=true)
	public String stack;

	@Override public Status execute() {
		EnemyBrain eb = getObject();
		switch (type) {
		case INT:
			IntStorage intStorage = eb.getIntStorage();
			return intStorage.getAndPush(varName, stack)?Status.SUCCEEDED:Status.FAILED;
		case FLOAT:
			FloatStorage floatStorage = eb.getFloatStorage();
			return floatStorage.getAndPush(varName, stack)?Status.SUCCEEDED:Status.FAILED;
		case STRING:
			StringStorage stringStorage = eb.getStringStorage();
			return stringStorage.getAndPush(varName, stack)?Status.SUCCEEDED:Status.FAILED;
		}
		return Status.FAILED;
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		StackPush range = (StackPush)task;
		range.varName = varName;
		range.stack = stack;
		range.type = type;
		return super.copyTo(task);
	}

	@Override public String getComment () {
		return "Get value from varName an push it to stack";
	}
}
