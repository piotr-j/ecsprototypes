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
public class ChangeStateTask extends BaseTask implements TaskComment {
	private final static String TAG = ChangeStateTask.class.getSimpleName();

	@TaskAttribute(required=true)
	public String name;

//	private ComponentMapper<EnemyBTree> mEnemyBTree;

	@Override public Status execute() {
		EnemyBrain brain = getObject();
		StackStateMachine<EnemyBrain, State<EnemyBrain>> fsm = brain.fsm;
		if (fsm == null) return Status.FAILED;
		State<EnemyBrain> state = brain.nameToState.get(name, null);
		if (state == null) return Status.FAILED;
		//EnemyBTree bTree = mEnemyBTree.get(brain.id);
		brain.fsm.changeState(state);
		return Status.SUCCEEDED;
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		ChangeStateTask range = (ChangeStateTask)task;
		range.name = name;
		return super.copyTo(task);
	}

	@Override public String getComment () {
		return "Change state";
	}
}
