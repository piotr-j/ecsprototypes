package io.piotrjastrzebski.ecsclones.restrainingofbob.utils;

import com.badlogic.gdx.ai.steer.Proximity;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.math.Vector2;

/**
 * {@link Proximity <Vector2>} implementation for use with box2d with circular check
 *
 * Created by PiotrJ on 12/09/15.
 */
public class PRadiusProximity extends PProximity {
	@Override protected boolean interestedIn (Steerable<Vector2> steerable) {
		// The bounding radius of the current body is taken into account
		// by adding it to the radius proximity
		float range = radius + steerable.getBoundingRadius();

		// Make sure the current body is within the range.
		// Notice we're working in distance-squared space to avoid square root.
		float distanceSquare = steerable.getPosition().dst2(owner.getPosition());

		return distanceSquare <= range * range;
	}
}
