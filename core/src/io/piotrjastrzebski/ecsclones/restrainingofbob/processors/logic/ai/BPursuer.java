package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.Manager;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.ai.steer.behaviors.*;
import com.badlogic.gdx.graphics.Color;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.status.Dead;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.SBehaviour;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.PSteerable;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.rendering.DebugTint;
import io.piotrjastrzebski.ecsclones.restrainingofbob.utils.MyBlendedSteering;

/**
 * Created by PiotrJ on 31/08/15.
 */
@Wire
public class BPursuer extends Manager {
	protected ComponentMapper<SBehaviour> mSBehaviour;
	protected ComponentMapper<DebugTint> mDebugTint;
	protected ComponentMapper<Dead> mDead;

	PSteerable dummy = new PSteerable();

	TagManager tags;
	@Override protected void initialize () {
	}

	public boolean set (int pursuer, String pursuee) {
		Entity target = tags.getEntity(pursuee);
		if (target == null) {
			return false;
		}
		mDebugTint.get(pursuer).set(Color.RED);
		return set(pursuer, target.getId());
	}

	public boolean set (int pursuer, int pursuee) {
		SBehaviour sBehaviour = mSBehaviour.create(pursuer);
//		if (mDead.has(target)) return false;
		sBehaviour.target = pursuee;
		MyBlendedSteering steering = new MyBlendedSteering(dummy);
		sBehaviour.behaviour = steering;
		steering.add(new LookWhereYouAreGoing<>(dummy), .5f);
		steering.add(new Pursue<>(dummy, null), .5f);
		return true;
	}

	public void stop (int eid) {
		mSBehaviour.remove(eid);
	}
}

