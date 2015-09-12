package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks;

import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.Finder;

/**
 * Created by PiotrJ on 19/08/15.
 */
@Wire
public class InRangeTask extends BaseTask {
	private final static String TAG = InRangeTask.class.getSimpleName();

	@TaskAttribute(required=true)
	public String target;

	@TaskAttribute(required=true)
	public float dst;

	Finder finder;

	@Override public void run () {
		EnemyBrain brain = getObject();
		if (finder.dst2(brain.id, target) < dst * dst) {
			success();
		} else {
			fail();
		}
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		InRangeTask range = (InRangeTask)task;
		range.target = target;
		range.finder = finder;
		range.dst = dst;
		return super.copyTo(task);
	}
}
