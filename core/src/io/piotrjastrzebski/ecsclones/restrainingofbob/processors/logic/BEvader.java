package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic;

import com.artemis.ComponentMapper;
import com.artemis.Manager;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.ai.steer.behaviors.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.SBehaviour;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.PSteerable;

/**
 * Created by PiotrJ on 31/08/15.
 */
@Wire
public class BEvader extends Manager {
	protected ComponentMapper<SBehaviour> mSBehaviour;
	protected ComponentMapper<PSteerable> mPSteerable;

	BlendedSteering<Vector2> behaviour;
	Evade<Vector2> evade;
	@Override protected void initialize () {
		PSteerable dummy = new PSteerable();
		behaviour = new BlendedSteering<>(dummy);
		behaviour.add(new LookWhereYouAreGoing<>(dummy), .5f);
		behaviour.add(evade = new Evade<>(dummy, null), .5f);
	}

	public void set (int evader, int evadee) {
		SBehaviour sBehaviour = mSBehaviour.get(evader);
		if (sBehaviour == null) {
			sBehaviour = world.getEntity(evader).edit().create(SBehaviour.class);
		}
		sBehaviour.behaviour = behaviour;
		sBehaviour.target = evadee;
//		evade.setTarget(mPSteerable.get(evadee));
	}
}
