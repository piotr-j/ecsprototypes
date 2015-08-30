package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import io.piotrjastrzebski.ecsclones.base.GameScreen;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.PBody;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.PSteerable;

/**
 * Created by PiotrJ on 23/08/15.
 */
@Wire
public class Steering extends EntityProcessingSystem {
	@Wire(name = GameScreen.WIRE_GAME_CAM) OrthographicCamera camera;
	@Wire ShapeRenderer renderer;

	protected ComponentMapper<PSteerable> mPSteerable;
	protected ComponentMapper<PBody> mPBody;


	public static final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<>(new Vector2());

	public Steering () {
		this(false);
	}

	private boolean debug;
	public Steering (boolean debug) {
		super(Aspect.all(PSteerable.class, PBody.class));
		this.debug = debug;
	}

	@Override protected void begin () {
		if (debug) {
			renderer.begin(ShapeRenderer.ShapeType.Line);
		}
	}

	@Override protected void process (Entity e) {
		PSteerable steerable = mPSteerable.get(e);
		if (steerable.behaviour == null) return;

		PBody phys = mPBody.get(e);
		Body body = phys.body;
		steerable.setBody(body);

		steerable.behaviour.calculateSteering(steeringOutput);

		boolean anyAccelerations = false;

		// Update position and linear velocity.
		if (!steeringOutput.linear.isZero()) {
			// this method internally scales the force by deltaTime
			body.applyForceToCenter(steeringOutput.linear, true);
			anyAccelerations = true;
		}

		// Update orientation and angular velocity
		if (steerable.isIndependentFacing()) {
			if (steeringOutput.angular != 0) {
				// this method internally scales the torque by deltaTime
				body.applyTorque(steeringOutput.angular, true);
				anyAccelerations = true;
			}
		} else {
			// If we haven't got any velocity, then we can do nothing.
			Vector2 linVel = steerable.getLinearVelocity();
			if (!linVel.isZero(steerable.getZeroLinearSpeedThreshold())) {
				float newOrientation = vectorToAngle(linVel);
				// this is superfluous if independentFacing is always true
				body.setAngularVelocity((newOrientation - steerable.getAngularVelocity()) * world.delta);
				body.setTransform(body.getPosition(), newOrientation);
			}
		}

		if (anyAccelerations) {
			// Cap the linear speed
			Vector2 velocity = body.getLinearVelocity();
			float currentSpeedSquare = velocity.len2();
			float maxLinearSpeed = steerable.getMaxLinearSpeed();
			if (currentSpeedSquare > maxLinearSpeed * maxLinearSpeed) {
				body.setLinearVelocity(velocity.scl(maxLinearSpeed / (float)Math.sqrt(currentSpeedSquare)));
			}

			// Cap the angular speed
			float maxAngVelocity = steerable.getMaxAngularSpeed();
			if (body.getAngularVelocity() > maxAngVelocity) {
				body.setAngularVelocity(maxAngVelocity);
			}
		}

		if (debug) {
			Vector2 pos = body.getPosition();
			SteeringBehavior<Vector2> behaviour = steerable.behaviour;
			if (behaviour instanceof Wander) {
				debugDraw((Wander<Vector2>)behaviour, pos);
			}
		}
	}

	private void debugDraw (Wander<Vector2> wander, Vector2 position) {
		Vector2 target = wander.getInternalTargetPosition();
		Vector2 center = wander.getWanderCenter();
		float radius = wander.getWanderRadius();
		renderer.setColor(Color.CYAN);
		renderer.line(position.x, position.y, center.x, center.y);
		renderer.circle(center.x, center.y, radius, 32);
		renderer.setColor(Color.RED);
		renderer.circle(target.x, target.y, 0.1f, 8);
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	@Override protected void end () {
		if (debug) {
			renderer.end();
		}
	}

	private float vectorToAngle (Vector2 vector) {
		return (float)Math.atan2(-vector.x, vector.y);
	}

	private Vector2 angleToVector (Vector2 outVector, float angle) {
		outVector.x = -(float)Math.sin(angle);
		outVector.y = (float)Math.cos(angle);
		return outVector;
	}
}
