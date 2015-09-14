package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic;

import com.artemis.ComponentMapper;
import com.artemis.Manager;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.ai.steer.behaviors.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.SBehaviour;
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

	TagManager tags;
	Evade<Vector2> evade;
	PSteerable dummy = new PSteerable();
	public boolean set (int evader, String evadee) {
		SBehaviour sBehaviour = mSBehaviour.get(evader);
		if (sBehaviour == null) {
			sBehaviour = world.getEntity(evader).edit().create(SBehaviour.class);
		}
		mDebugTint.get(evader).color.set(Color.PURPLE);

		sBehaviour.target = tags.getEntity(evadee).id;

		MyBlendedSteering behaviour = new MyBlendedSteering(dummy);
		sBehaviour.behaviour = behaviour;
		behaviour.add(new LookWhereYouAreGoing<>(dummy), .5f);
		behaviour.add(evade = new Evade<>(dummy, null), .5f);
		return true;
	}
}
