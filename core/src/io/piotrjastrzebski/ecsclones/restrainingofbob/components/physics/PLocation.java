package io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics;

import com.artemis.Component;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai.Steering;

/**
 * Created by PiotrJ on 23/08/15.
 */
public class PLocation extends Component implements Location<Vector2> {
	Vector2 position = new Vector2();
	float orientation;

	@Override
	public Vector2 getPosition () {
		return position;
	}

	@Override
	public float getOrientation () {
		return orientation;
	}

	@Override
	public void setOrientation (float orientation) {
		this.orientation = orientation;
	}

	@Override
	public Location<Vector2> newLocation () {
		return new PLocation();
	}

	@Override
	public float vectorToAngle (Vector2 vector) {
		return Steering.vectorToAngle(vector);
	}

	@Override
	public Vector2 angleToVector (Vector2 outVector, float angle) {
		return Steering.angleToVector(outVector, angle);
	}
}
