package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai;

import com.artemis.ComponentMapper;
import com.artemis.Manager;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.behaviors.Separation;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.SBehaviour;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.PSteerable;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.rendering.DebugTint;
import io.piotrjastrzebski.ecsclones.restrainingofbob.utils.PProximity;
import io.piotrjastrzebski.ecsclones.restrainingofbob.utils.MyPrioritySteering;

/**
 * Created by PiotrJ on 31/08/15.
 */
@Wire
public class BWanderer extends Manager {
	protected ComponentMapper<SBehaviour> mSBehaviour;
	protected ComponentMapper<DebugTint> mDebugTint;

	Steerable<Vector2> dummy = new PSteerable();
	PProximity dummyProxy = new PProximity();

	public void set (int id) {
		SBehaviour sBehaviour = mSBehaviour.create(id);
		sBehaviour.target = -1;
		mDebugTint.get(id).setDdefault();
		// TODO pool?
		MyPrioritySteering priority = new MyPrioritySteering(dummy, 0.001f);

		Separation<Vector2> separation = new Separation<>(dummy, dummyProxy);
		priority.add(separation);
		Wander<Vector2> wander = new Wander<>(dummy); //
		wander.setFaceEnabled(true) // We want to use Face internally (independent facing is on)
			.setAlignTolerance(0.001f) // Used by Face
			.setDecelerationRadius(0.25f) // Used by Face
			.setTimeToTarget(0.1f) // Used by Face
			.setWanderOffset(6f) //
			.setWanderOrientation(MathUtils.random(360)) //
			.setWanderRadius(2f) //
			.setWanderRate(MathUtils.PI2 * 40);

		priority.add(wander);
		sBehaviour.behaviour = priority;
	}
}
