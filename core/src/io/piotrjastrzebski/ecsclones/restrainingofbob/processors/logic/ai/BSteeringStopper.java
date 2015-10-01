package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai;

import com.artemis.*;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.ai.steer.behaviors.LookWhereYouAreGoing;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.graphics.Color;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.SBehaviour;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.status.Dead;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.PSteerable;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.rendering.DebugTint;
import io.piotrjastrzebski.ecsclones.restrainingofbob.utils.MyBlendedSteering;

/**
 * Created by PiotrJ on 31/08/15.
 */
@Wire
public class BSteeringStopper extends Manager {
	protected ComponentMapper<SBehaviour> mSBehaviour;
	protected ComponentMapper<DebugTint> mDebugTint;

	EntityTransmuter rmSB;
	@Override protected void initialize () {
		rmSB = new EntityTransmuterFactory(world).remove(SBehaviour.class).build();
	}

	public boolean set (int id) {
		SBehaviour sBehaviour = mSBehaviour.getSafe(id);
		if (sBehaviour != null) {
			rmSB.transmute(world.getEntity(id));
			mDebugTint.get(id).setDdefault();
		}
		return true;
	}
}

