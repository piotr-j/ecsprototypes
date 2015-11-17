package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.actions;

import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.btree.Task;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.Meleer;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.Shooter;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.base.BaseTask;

/**
 * Created by PiotrJ on 19/08/15.
 */
@Wire(injectInherited = true)
public class AttackCoolDownTask extends BaseTask {
	private final static String TAG = AttackCoolDownTask.class.getSimpleName();

	private ComponentMapper<Shooter> mShooter;
	private ComponentMapper<Meleer> mMeleer;

	private float startTime;
	private float timeout;

	@Override
	public void start () {
		EnemyBrain brain = getObject();
		timeout = 9999;
		// really need to make this simpler...
		// lets assume for now that enemy can be only one of these at a time
		if (mMeleer.has(brain.id)) {
			timeout = mMeleer.get(brain.id).delay;
		} else if (mShooter.has(brain.id)) {
			timeout = mShooter.get(brain.id).delay;
		}
		startTime = GdxAI.getTimepiece().getTime();
	}

	@Override public Status execute() {
		return GdxAI.getTimepiece().getTime() - startTime < timeout ? Status.RUNNING : Status.SUCCEEDED;
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		AttackCoolDownTask melee = (AttackCoolDownTask)task;
		melee.mShooter = mShooter;
		melee.mMeleer = mMeleer;
		return super.copyTo(task);
	}
}
