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
public class ValueIsEmpty extends BaseTask {

	@TaskAttribute(required=true)
	public String name;

	@TaskAttribute(required=true)
	public ValueType type = ValueType.INT;

	@Override public Status execute() {
		EnemyBrain eb = getObject();
		switch (type) {
		case INT:
			return eb.getIntStorage().hasValue(name)?Status.FAILED:Status.SUCCEEDED;
		case FLOAT:
			return eb.getFloatStorage().hasValue(name)?Status.FAILED:Status.SUCCEEDED;
		case STRING:
			return eb.getStringStorage().hasValue(name)?Status.FAILED:Status.SUCCEEDED;
		}
		return Status.FAILED;
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		ValueIsEmpty range = (ValueIsEmpty)task;
		range.name = name;
		return super.copyTo(task);
	}
}
