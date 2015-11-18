package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.actions;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import com.badlogic.gdx.utils.IntArray;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.Finder;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.base.BaseTask;

/**
 * Created by PiotrJ on 19/08/15.
 */
@Wire(injectInherited = true)
public class FindGroupTask extends BaseTask {
	private final static String TAG = FindGroupTask.class.getSimpleName();

	@Wire
	Finder finder;

	@TaskAttribute(required=true)
	public String intStack;

	@TaskAttribute(required=true)
	public String group;

	@TaskAttribute(required=true)
	public float distance;

	@Override public Status execute() {
		EnemyBrain brain = getObject();
		IntArray found = finder.findTaggedWithin(brain.id, group, distance);
		if (found.size > 0) {
			IntArray intArray = brain.getIntStorage().getStack(intStack);
			intArray.addAll(found);
			return Status.SUCCEEDED;
		} else {
			return Status.FAILED;
		}
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		FindGroupTask melee = (FindGroupTask)task;
		melee.group = group;
		melee.intStack = intStack;
		melee.distance = distance;
		melee.finder = finder;
		return super.copyTo(task);
	}
}
