package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.actions;

import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.status.Dead;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.status.Health;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai.storage.IntStorage;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.base.BaseTask;

/**
 * Created by PiotrJ on 19/08/15.
 */
@Wire(injectInherited = true)
public class HealTask extends BaseTask {
	private final static String TAG = HealTask.class.getSimpleName();

	@TaskAttribute(required=true)
	public String target;

	private ComponentMapper<Health> mHealth;
	private ComponentMapper<Dead> mDead;

	private float startTime;
	private float timeout;

	@Override public void start () {
		EnemyBrain brain = getObject();
		IntStorage storage = brain.getIntStorage();
		if (storage.hasValue(target)) {
			startTime = GdxAI.getTimepiece().getTime();
			// this should probably come from somewhere, like heal type or whatever
			timeout = 1;
		}
	}

	@Override public Status execute() {
		EnemyBrain brain = getObject();
		IntStorage storage = brain.getIntStorage();
		if (!storage.hasValue(target)) return Status.FAILED;
		int targetId = storage.getValue(target);
		if (mDead.has(targetId)) return Status.FAILED;

		if (GdxAI.getTimepiece().getTime() - startTime < timeout) {
			return Status.RUNNING;
		} else {
			// lets assume all target has this for now
			// this probably should be a system of sorts
			Health health = mHealth.get(targetId);
			health.addHP(1f);
			return Status.SUCCEEDED;
		}
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		HealTask melee = (HealTask)task;
		melee.target = target;
		melee.mDead = mDead;
		melee.mHealth = mHealth;
		return super.copyTo(task);
	}
}
