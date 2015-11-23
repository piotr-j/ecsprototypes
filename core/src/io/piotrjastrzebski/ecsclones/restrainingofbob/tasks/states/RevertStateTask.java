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
public class RevertStateTask extends BaseTask implements TaskComment {
	private final static String TAG = RevertStateTask.class.getSimpleName();

	@Override public Status execute() {
		EnemyBrain brain = getObject();

		StackStateMachine<EnemyBrain, State<EnemyBrain>> fsm = brain.fsm;
		if (fsm == null) return Status.FAILED;
		return fsm.revertToPreviousState()?Status.SUCCEEDED:Status.FAILED;
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		RevertStateTask range = (RevertStateTask)task;
		return super.copyTo(task);
	}

	@Override public String getComment () {
		return "Change state to previous one if there is one";
	}
}
