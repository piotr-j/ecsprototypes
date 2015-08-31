package io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics;

import com.artemis.Component;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

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

//	public SteeringBehavior<Vector2> behaviour;
	private boolean independentFacing;

//	public void setBehaviour (Wander<Vector2> behaviour) {
//		this.behaviour = behaviour;
//	}

	public void setBoundingRadius (float boundingRadius) {
		this.boundingRadius = boundingRadius;
	}

	public Body getBody () {
		return body;
	}

	public void setBody (Body body) {
		this.body = body;
	}

	@Override
	public Vector2 getPosition () {
		return body.getPosition();
	}

	@Override
	public float getOrientation () {
		return body.getAngle();
	}

	@Override public Vector2 getLinearVelocity () {
		return body.getLinearVelocity();
	}

	@Override public float getAngularVelocity () {
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

	@Override public Vector2 newVector () {
		return new Vector2();
	}

	@Override public float vectorToAngle (Vector2 vector) {
		return (float)Math.atan2(-vector.x, vector.y);
	}

	@Override public Vector2 angleToVector (Vector2 outVector, float angle) {
		outVector.x = -(float)Math.sin(angle);
		outVector.y = (float)Math.cos(angle);
		return outVector;
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
		return 0.001f;
	}

	public boolean isIndependentFacing () {
		return independentFacing;
	}

	public void setIndependentFacing (boolean independentFacing) {
		this.independentFacing = independentFacing;
	}
}
