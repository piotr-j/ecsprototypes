package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai;

import com.artemis.*;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ai.GdxAI;
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
import io.piotrjastrzebski.ecsclones.base.util.Input;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.BTWatcher;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.status.Dead;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.SBehaviour;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.PBody;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.PSteerable;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.physics.Physics;
import io.piotrjastrzebski.ecsclones.restrainingofbob.utils.MyBlendedSteering;
import io.piotrjastrzebski.ecsclones.restrainingofbob.utils.MyPrioritySteering;
import io.piotrjastrzebski.ecsclones.restrainingofbob.utils.PProximity;

/**
 * Created by PiotrJ on 23/08/15.
 */
@Wire
public class Steering extends IteratingSystem implements Input, InputProcessor {
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
		super(Aspect.all(PSteerable.class, PBody.class, SBehaviour.class).exclude(Dead.class, BTWatcher.class));
		this.debug = debug;
	}

	@Override protected void begin () {
		if (debug) {
			renderer.begin(ShapeRenderer.ShapeType.Line);
		}
		// need to update or steering will be broken
		GdxAI.getTimepiece().update(world.delta);
	}

	@Override protected void process (int eid) {
		PSteerable steerable = mPSteerable.get(eid);

		PBody phys = mPBody.get(eid);
		Body body = phys.body;
		steerable.setBody(body);

		SBehaviour sBehaviour = mSBehaviour.get(eid);
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
			PSteerable target = mPSteerable.getSafe(sBehaviour.target);
			if (target != null) {
				PBody tb = mPBody.getSafe(sBehaviour.target);
				if (tb != null)
					target.setBody(tb.body);
				setTarget(sBehaviour.behaviour, target);
			}
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
			renderer.setProjectionMatrix(camera.combined);
			renderer.begin(ShapeRenderer.ShapeType.Line);
			debugDraw(sBehaviour.behaviour, body.getPosition());
			renderer.end();
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
			Proximity<Vector2> proximity = ((GroupBehavior<Vector2>)behaviour).getProximity();
			proximity.setOwner(steerable);
			if (proximity instanceof PProximity) {
				((PProximity)proximity).setWorld(physics.getB2DWorld());
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

	@Override public int priority () {
		return 10;
	}

	@Override public InputProcessor get () {
		return this;
	}

	@Override public boolean keyDown (int keycode) {
		switch (keycode) {
		case com.badlogic.gdx.Input.Keys.F2:
			debug = !debug;
			break;
		}
		return false;
	}

	@Override public boolean keyUp (int keycode) {
		return false;
	}

	@Override public boolean keyTyped (char character) {
		return false;
	}

	@Override public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override public boolean touchDragged (int screenX, int screenY, int pointer) {
		return false;
	}

	@Override public boolean mouseMoved (int screenX, int screenY) {
		return false;
	}

	@Override public boolean scrolled (int amount) {
		return false;
	}
}
