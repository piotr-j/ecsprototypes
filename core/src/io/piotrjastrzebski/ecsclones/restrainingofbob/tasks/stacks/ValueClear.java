package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.stacks;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.base.BaseTask;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.base.ValueType;

/**
 * Created by PiotrJ on 19/08/15.
 */
@Wire(injectInherited = true)
public class ValueClear extends BaseTask {

	@TaskAttribute(required=true)
	public String varName;

	@TaskAttribute(required=true)
	public ValueType type = ValueType.INT;

	@Override public Status execute() {
		EnemyBrain eb = getObject();
		switch (type) {
		case INT:
			return eb.getIntStorage().clearValue(varName)?Status.SUCCEEDED:Status.FAILED;
		case FLOAT:
			return eb.getFloatStorage().clearValue(varName)?Status.SUCCEEDED:Status.FAILED;
		case STRING:
			return eb.getStringStorage().clearValue(varName)?Status.SUCCEEDED:Status.FAILED;
		}
		return Status.FAILED;
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		ValueClear range = (ValueClear)task;
		range.varName = varName;
		return super.copyTo(task);
	}
}
