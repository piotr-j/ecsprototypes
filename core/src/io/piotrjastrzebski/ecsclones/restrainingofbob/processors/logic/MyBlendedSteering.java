package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by PiotrJ on 01/09/15.
 */
public class MyBlendedSteering extends BlendedSteering<Vector2> {
	public MyBlendedSteering (Steerable<Vector2> owner) {
		super(owner);
	}
	Array<SteeringBehavior<Vector2>> behaviors = new Array<>();
	public Array<SteeringBehavior<Vector2>> getBehaviours () {
		if (behaviors.size == 0) {
			for (BehaviorAndWeight<Vector2> behaviorAndWeight : list) {
				behaviors.add(behaviorAndWeight.getBehavior());
			}
		}
		return behaviors;
	}
}
