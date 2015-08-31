package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic;

import com.artemis.ComponentMapper;
import com.artemis.Manager;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.ai.steer.behaviors.*;
import com.badlogic.gdx.math.Vector2;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.SBehaviour;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.PSteerable;

/**
 * Created by PiotrJ on 31/08/15.
 */
@Wire
public class BPursuer extends Manager {
	protected ComponentMapper<SBehaviour> mSBehaviour;
//	protected ComponentMapper<PSteerable> mPSteerable;

	BlendedSteering<Vector2> behaviour;
	Pursue<Vector2> pursue;
	Finder finder;

	TagManager tags;
	@Override protected void initialize () {
		PSteerable dummy = new PSteerable();
		behaviour = new BlendedSteering<>(dummy);
		behaviour.add(new LookWhereYouAreGoing<>(dummy), .5f);
		behaviour.add(pursue = new Pursue<>(dummy, null), .5f);
	}

	public boolean set (int pursuer, String pursuee) {
		SBehaviour sBehaviour = mSBehaviour.get(pursuer);
		if (sBehaviour == null) {
			sBehaviour = world.getEntity(pursuer).edit().create(SBehaviour.class);
		}

//		if (finder.dst2(pursuer, pursuee) > 100)
//			return false;
		sBehaviour.target = tags.getEntity(pursuee).id;
		sBehaviour.behaviour = behaviour;
//		pursue.setTarget(mPSteerable.get(pursuee));
		return true;
	}
}

