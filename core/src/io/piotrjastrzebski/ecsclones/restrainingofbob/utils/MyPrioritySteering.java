package io.piotrjastrzebski.ecsclones.restrainingofbob.utils;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by PiotrJ on 01/09/15.
 */
public class MyPrioritySteering extends PrioritySteering<Vector2> {
	public MyPrioritySteering (Steerable<Vector2> owner, float radius) {
		super(owner, radius);
	}

	public Array<SteeringBehavior<Vector2>> getBehaviours () {
		return behaviors;
	}
}
