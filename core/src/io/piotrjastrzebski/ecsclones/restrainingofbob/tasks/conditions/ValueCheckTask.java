package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.conditions;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import io.piotrjastrzebski.bteditor.core.TaskComment;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai.storage.FloatStorage;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai.storage.IntStorage;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai.storage.StringStorage;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.base.BaseTask;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.base.ComparisonType;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.base.ValueType;

/**
 * Created by PiotrJ on 19/08/15.
 */
@Wire(injectInherited = true)
public class ValueCheckTask extends BaseTask implements TaskComment {
	private final static String TAG = ValueCheckTask.class.getSimpleName();

	@TaskAttribute(required=true)
	public String varName;

	@TaskAttribute(required=true)
	public ValueType varType = ValueType.INT;

	@TaskAttribute(required=true)
	public ComparisonType compType = ComparisonType.GT;

	@TaskAttribute
	public String otherVarName;

	@TaskAttribute
	public int intVal;
	@TaskAttribute
	public float floatVal;
	@TaskAttribute
	public String stringVal;

	@Override public Status execute () {
		EnemyBrain brain = getObject();
		switch (varType) {
		case INT:
			IntStorage intStorage = brain.getIntStorage();
			if (!intStorage.hasValue(varName))
				return Status.FAILED;
			int intVar = intStorage.getValue(varName);
			return compType.compare(intVar, getOtherInt(intStorage)) ? Status.SUCCEEDED : Status.FAILED;
		case FLOAT:
			FloatStorage floatStorage = brain.getFloatStorage();
			if (!floatStorage.hasValue(varName))
				return Status.FAILED;
			float floatVar = floatStorage.getValue(varName);
			return compType.compare(floatVar, getOtherFloat(floatStorage)) ? Status.SUCCEEDED : Status.FAILED;
		case STRING:
			StringStorage stringStorage = brain.getStringStorage();
			if (!stringStorage.hasValue(varName))
				return Status.FAILED;
			String stringVar = stringStorage.getValue(varName);
			return compType.compare(stringVar, getOtherString(stringStorage)) ? Status.SUCCEEDED : Status.FAILED;
		}
		return Status.FAILED;
	}

	public int getOtherInt (IntStorage storage) {
		if (otherVarName != null && storage.hasValue(otherVarName)) {
			return storage.getValue(otherVarName);
		}
		return intVal;
	}

	public float getOtherFloat (FloatStorage storage) {
		if (otherVarName != null && storage.hasValue(otherVarName)) {
			return storage.getValue(otherVarName);
		}
		return floatVal;
	}

	public String getOtherString (StringStorage storage) {
		if (otherVarName != null && storage.hasValue(otherVarName)) {
			return storage.getValue(otherVarName);
		}
		return stringVal;
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		ValueCheckTask range = (ValueCheckTask)task;
		range.varName = varName;
		range.compType = compType;
		range.varType = varType;
		range.intVal = intVal;
		range.floatVal = floatVal;
		range.stringVal = stringVal;
		return super.copyTo(task);
	}

	@Override public String getComment () {
		return "Test value against other with set comparison type, other from storage is preferred";
	}
}
