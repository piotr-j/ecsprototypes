package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.states;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import com.badlogic.gdx.ai.fsm.StackStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import io.piotrjastrzebski.bteditor.core.TaskComment;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.base.BaseTask;

/**
 * Created by PiotrJ on 19/08/15.
 */
@Wire(injectInherited = true)
public class GetStateTask extends BaseTask implements TaskComment {
	private final static String TAG = GetStateTask.class.getSimpleName();

	@TaskAttribute(required=true)
	public String stateVar;

//	private ComponentMapper<EnemyBTree> mEnemyBTree;

	@Override public Status execute() {
		EnemyBrain brain = getObject();
		StackStateMachine<EnemyBrain, State<EnemyBrain>> fsm = brain.fsm;
		if (fsm == null) return Status.FAILED;

		String name = brain.stateToName.get(fsm.getCurrentState(), null);
		if (name == null) return Status.FAILED;
		brain.getStringStorage().putValue(stateVar, name);
		return Status.SUCCEEDED;
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		GetStateTask range = (GetStateTask)task;
		range.stateVar = stateVar;
		return super.copyTo(task);
	}

	@Override public String getComment () {
		return "Put state name into variable";
	}
}
