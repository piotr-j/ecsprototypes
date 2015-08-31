package io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics;

import com.artemis.Component;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.Steering;

/**
 * Created by PiotrJ on 23/08/15.
 */
public class PSteerable extends Component implements Steerable<Vector2> {
	Body body;

	float boundingRadius;
	boolean tagged;

	float maxLinearSpeed;
	float maxLinearAcceleration;
	float maxAngularSpeed;
	float maxAngularAcceleration;
	float zeroThreshold = 0.001f;

	private boolean independentFacing;

	public void setBoundingRadius (float boundingRadius) {
		this.boundingRadius = boundingRadius;
	}

	public Body getBody () {
		return body;
	}

	public void setBody (Body body) {
		this.body = body;
	}

	private Vector2 zero = new Vector2();
	@Override
	public Vector2 getPosition () {
		if (body == null) return zero;
		return body.getPosition();
	}

	@Override
	public float getOrientation () {
		if (body == null) return 0;
		return body.getAngle();
	}

	@Override public void setOrientation (float orientation) {

	}

	@Override public Vector2 getLinearVelocity () {
		if (body == null) return zero;
		return body.getLinearVelocity();
	}

	@Override public float getAngularVelocity () {
		if (body == null) return 0;
		return body.getAngularVelocity();
	}

	@Override public float getBoundingRadius () {
		return boundingRadius;
	}

	@Override public boolean isTagged () {
		return tagged;
	}

	@Override public void setTagged (boolean tagged) {
		this.tagged = tagged;
	}

//	@Override public Vector2 newVector () {
//		return new Vector2();
//	}

	@Override public float vectorToAngle (Vector2 vector) {
		return Steering.vectorToAngle(vector);
	}

	@Override public Vector2 angleToVector (Vector2 outVector, float angle) {
		return Steering.angleToVector(outVector, angle);
	}

	@Override public Location<Vector2> newLocation () {
		return new PLocation();
	}

	@Override public float getMaxLinearSpeed () {
		return maxLinearSpeed;
	}

	@Override public void setMaxLinearSpeed (float maxLinearSpeed) {
		this.maxLinearSpeed = maxLinearSpeed;
	}

	@Override public float getMaxLinearAcceleration () {
		return maxLinearAcceleration;
	}

	@Override public void setMaxLinearAcceleration (float maxLinearAcceleration) {
		this.maxLinearAcceleration = maxLinearAcceleration;
	}

	@Override public float getMaxAngularSpeed () {
		return maxAngularSpeed;
	}

	@Override public void setMaxAngularSpeed (float maxAngularSpeed) {
		this.maxAngularSpeed = maxAngularSpeed;
	}

	@Override public float getMaxAngularAcceleration () {
		return maxAngularAcceleration;
	}

	@Override public void setMaxAngularAcceleration (float maxAngularAcceleration) {
		this.maxAngularAcceleration = maxAngularAcceleration;
	}

	public float getZeroLinearSpeedThreshold () {
		return zeroThreshold;
	}

	@Override public void setZeroLinearSpeedThreshold (float zeroThreshold) {
		this.zeroThreshold = zeroThreshold;
	}

	public boolean isIndependentFacing () {
		return independentFacing;
	}

	public void setIndependentFacing (boolean independentFacing) {
		this.independentFacing = independentFacing;
	}
}
