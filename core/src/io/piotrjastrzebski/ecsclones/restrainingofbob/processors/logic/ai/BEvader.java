package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.Manager;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.ai.steer.behaviors.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.status.Dead;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.SBehaviour;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.PSteerable;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.rendering.DebugTint;
import io.piotrjastrzebski.ecsclones.restrainingofbob.utils.MyBlendedSteering;

/**
 * Created by PiotrJ on 31/08/15.
 */
@Wire
public class BEvader extends Manager {
	protected ComponentMapper<SBehaviour> mSBehaviour;
	protected ComponentMapper<DebugTint> mDebugTint;
	protected ComponentMapper<Dead> mDead;
	TagManager tags;
	Evade<Vector2> evade;
	PSteerable dummy = new PSteerable();
	public boolean set (int evader, String evadee) {
		Entity target = tags.getEntity(evadee);
		if (target == null) {
			return false;
		}
		return set(evader, target.getId());
	}

	public boolean set (int evader, int evadee) {
		SBehaviour sBehaviour = mSBehaviour.create(evader);
//		if (mDead.has(target)) return false;
		mDebugTint.get(evader).color.set(Color.PURPLE);

		sBehaviour.target = evadee;

		MyBlendedSteering behaviour = new MyBlendedSteering(dummy);
		sBehaviour.behaviour = behaviour;
		behaviour.add(new LookWhereYouAreGoing<>(dummy), .5f);
		behaviour.add(evade = new Evade<>(dummy, null), .5f);
		return true;
	}
}
