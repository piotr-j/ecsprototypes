package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.base.BaseTask;

/**
 * Created by PiotrJ on 19/08/15.
 */
@Wire(injectInherited = true)
public class StackPopTask extends BaseTask {
	private final static String TAG = StackPopTask.class.getSimpleName();

	@TaskAttribute(required=true)
	public String name;

	@Override public void run () {
		EnemyBrain eb = getObject();
//		eb.getStack(name);
//		if (stack.isEmpty()) {
//			fail();
//		} else {
//		   eb.object = stack.pop();
//			success();
//		}
		success();
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		StackPopTask range = (StackPopTask)task;
		range.name = name;
		return super.copyTo(task);
	}
}
