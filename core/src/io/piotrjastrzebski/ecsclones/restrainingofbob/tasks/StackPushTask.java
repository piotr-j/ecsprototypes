package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.base.BaseTask;

/**
 * Created by PiotrJ on 19/08/15.
 */
@Wire(injectInherited = true)
public class StackPushTask extends BaseTask {
	private final static String TAG = StackPushTask.class.getSimpleName();

	@TaskAttribute(required=true)
	public String name;

	@Override public void run () {
		EnemyBrain eb = getObject();

//		Stack stack = eb.getStack(name);
//		eb.object = stack.pop();
		success();
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		StackPushTask range = (StackPushTask)task;
		range.name = name;
		return super.copyTo(task);
	}
}
