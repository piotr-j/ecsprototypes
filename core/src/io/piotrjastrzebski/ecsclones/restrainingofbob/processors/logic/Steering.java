package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic;

import com.artemis.*;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.ai.steer.GroupBehavior;
import com.badlogic.gdx.ai.steer.Proximity;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import io.piotrjastrzebski.ecsclones.base.GameScreen;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.Dead;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.Player;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.SBehaviour;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.PBody;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.PSteerable;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.physics.Physics;
import io.piotrjastrzebski.ecsclones.restrainingofbob.utils.Box2dRadiusProximity;
import io.piotrjastrzebski.ecsclones.restrainingofbob.utils.Box2dSquareAABBProximity;
import io.piotrjastrzebski.ecsclones.restrainingofbob.utils.MyBlendedSteering;
import io.piotrjastrzebski.ecsclones.restrainingofbob.utils.MyPrioritySteering;

/**
 * Created by PiotrJ on 23/08/15.
 */
@Wire
public class Steering extends EntityProcessingSystem {
	@Wire(name = GameScreen.WIRE_GAME_CAM) OrthographicCamera camera;
	@Wire ShapeRenderer renderer;

	protected ComponentMapper<PSteerable> mPSteerable;
	protected ComponentMapper<PBody> mPBody;
	protected ComponentMapper<SBehaviour> mSBehaviour;

	protected Physics physics;

	public static final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<>(new Vector2());

	public Steering () {
		this(false);
	}

	private boolean debug;
	public Steering (boolean debug) {
		super(Aspect.all(PSteerable.class, PBody.class, SBehaviour.class).exclude(Dead.class));
		this.debug = debug;
	}

	EntitySubscription target;
	@Override protected void initialize () {
		target = world.getManager(AspectSubscriptionManager.class)
			.get(Aspect.all(Player.class, PSteerable.class, PBody.class));
	}

	PSteerable targetSteerable;
	@Override protected void begin () {
		if (debug) {
			renderer.begin(ShapeRenderer.ShapeType.Line);
		}
		IntBag entities = target.getEntities();
		if (entities.size() > 0) {
			int id = entities.get(0);
			targetSteerable = mPSteerable.get(id);
			targetSteerable.setBody(mPBody.get(id).body);
		} else {
			target = null;
		}
	}

	@Override protected void process (Entity e) {
		PSteerable steerable = mPSteerable.get(e);

		PBody phys = mPBody.get(e);
		Body body = phys.body;
		steerable.setBody(body);

		SBehaviour sBehaviour = mSBehaviour.get(e);
		if (sBehaviour.behaviour == null) return;

//		if (sBehaviour.behaviour instanceof BlendedSteering) {
//			BlendedSteering<Vector2> blended = (BlendedSteering<Vector2>)sBehaviour.behaviour;
//			for (int i = 0; i < sBehaviour.size; i++) {
//				SteeringBehavior<Vector2> behavior = blended.get(i).getBehavior();
//				if (behavior instanceof Pursue) {
//					if (targetSteerable == null) return;
//					((Pursue<Vector2>)behavior).setTarget(targetSteerable);
//				}
//			}
//		}
		setOwner(sBehaviour.behaviour, steerable);
		if (sBehaviour.target >= 0) {
			setTarget(sBehaviour.behaviour, mPSteerable.get(sBehaviour.target));
		}
		sBehaviour.behaviour.calculateSteering(steeringOutput);

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
			debugDraw(sBehaviour.behaviour, body.getPosition());
		}
	}

	private void debugDraw (SteeringBehavior<Vector2> behaviour, Vector2 pos) {
		if (behaviour instanceof MyBlendedSteering) {
			MyBlendedSteering blended = (MyBlendedSteering)behaviour;
			for (SteeringBehavior<Vector2> b : blended.getBehaviours()) {
				debugDraw(b, pos);
			}
		} else if (behaviour instanceof MyPrioritySteering) {
			MyPrioritySteering priority = (MyPrioritySteering)behaviour;
			for (SteeringBehavior<Vector2> b : priority.getBehaviours()) {
				debugDraw(b, pos);
			}
		} else if (behaviour instanceof Wander) {
			debugDraw((Wander<Vector2>)behaviour, pos);
		} else if (behaviour instanceof Pursue) {
			debugDraw((Pursue<Vector2>)behaviour, pos);
		} if (behaviour instanceof Pursue) {
			debugDraw((Pursue<Vector2>)behaviour, pos);
		}
	}

	private void debugDraw (Wander<Vector2> wander, Vector2 pos) {
		Vector2 target = wander.getInternalTargetPosition();
		Vector2 center = wander.getWanderCenter();
		float radius = wander.getWanderRadius();
		renderer.setColor(Color.CYAN);
		renderer.line(pos.x, pos.y, center.x, center.y);
		renderer.circle(center.x, center.y, radius, 32);
		renderer.setColor(Color.RED);
		renderer.circle(target.x, target.y, 0.1f, 8);
	}

	private void debugDraw (Pursue<Vector2> pursue, Vector2 pos) {
		Vector2 target = pursue.getTarget().getPosition();
		renderer.setColor(Color.RED);
		renderer.line(pos.x, pos.y, target.x, target.y);
	}

	private void setOwner (SteeringBehavior<Vector2> behaviour, PSteerable steerable) {
		behaviour.setOwner(steerable);
		if (behaviour instanceof MyBlendedSteering) {
			MyBlendedSteering blended = (MyBlendedSteering)behaviour;
			for (SteeringBehavior<Vector2> b : blended.getBehaviours()) {
				setOwner(b, steerable);
			}
		} else if (behaviour instanceof MyPrioritySteering) {
			MyPrioritySteering priority = (MyPrioritySteering)behaviour;
			for (SteeringBehavior<Vector2> b : priority.getBehaviours()) {
				setOwner(b, steerable);
			}
		} else if (behaviour instanceof GroupBehavior) {
			Proximity proximity = ((GroupBehavior)behaviour).getProximity();
			proximity.setOwner(steerable);
			if (proximity instanceof Box2dSquareAABBProximity) {
				((Box2dSquareAABBProximity)proximity).setWorld(physics.getWorld());
			}else if (proximity instanceof Box2dRadiusProximity) {
				((Box2dRadiusProximity)proximity).setWorld(physics.getWorld());
			}
		}
	}

	private void setTarget (SteeringBehavior<Vector2> behaviour, PSteerable steerable) {
		if (behaviour instanceof MyBlendedSteering) {
			MyBlendedSteering blended = (MyBlendedSteering)behaviour;
			for (SteeringBehavior<Vector2> b : blended.getBehaviours()) {
				setTarget(b, steerable);
			}
		} else if (behaviour instanceof MyPrioritySteering) {
			MyPrioritySteering priority = (MyPrioritySteering)behaviour;
			for (SteeringBehavior<Vector2> b : priority.getBehaviours()) {
				setTarget(b, steerable);
			}
		} else if (behaviour instanceof Evade) {
			((Evade<Vector2>)behaviour).setTarget(steerable);
		} else if (behaviour instanceof Pursue) {
			((Pursue<Vector2>)behaviour).setTarget(steerable);
		}
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	@Override protected void end () {
		if (debug) {
			renderer.end();
		}
	}

	public static float vectorToAngle (Vector2 vector) {
		return (float)Math.atan2(-vector.x, vector.y);
	}

	public static Vector2 angleToVector (Vector2 outVector, float angle) {
		outVector.x = -(float)Math.sin(angle);
		outVector.y = (float)Math.cos(angle);
		return outVector;
	}
}
