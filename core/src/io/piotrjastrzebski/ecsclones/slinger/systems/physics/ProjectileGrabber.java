package io.piotrjastrzebski.ecsclones.slinger.systems.physics;

import com.artemis.*;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import io.piotrjastrzebski.ecsclones.slinger.SlingerScreen;
import io.piotrjastrzebski.ecsclones.slinger.components.Slinging;
import io.piotrjastrzebski.ecsclones.slinger.components.parts.Projectile;
import io.piotrjastrzebski.ecsclones.slinger.components.parts.Sling;
import io.piotrjastrzebski.ecsclones.base.util.Input;

/**
 * Created by EvilEntity on 15/08/2015.
 */
@Wire
public class ProjectileGrabber extends EntityProcessingSystem implements Input {
	private ComponentMapper<Projectile> mProjectile;
	private ComponentMapper<Slinging> mSlinging;
	private ComponentMapper<Sling> mSling;

	@Wire Physics physics;
	@Wire(name = SlingerScreen.WIRE_GAME_CAM) OrthographicCamera gameCamera;

	public ProjectileGrabber () {
		super(Aspect.all(Projectile.class, Slinging.class));
	}

	@Override protected void initialize () {
		super.initialize();

	}

	Body targetBody;
	boolean grabbed;
	boolean released;
	MouseJointDef mouseJointDef = new MouseJointDef();
	@Override protected void process (Entity e) {
		Projectile projectile = mProjectile.get(e);
		Slinging slinging = mSlinging.get(e);
		Sling sling = mSling.get(slinging.slingID);
		targetBody = projectile.body;
		physics.getWorld().QueryAABB(callback, temp.x - 0.01f, temp.y - 0.01f, temp.x + 0.01f, temp.y + 0.01f);
		if (grabbed && !dragging) {
			dragging = true;
			grabbed = false;
			mouseJointDef.bodyA = sling.body;
			mouseJointDef.bodyB = projectile.body;
			mouseJointDef.collideConnected = true;
			mouseJointDef.target.set(projectile.body.getPosition());
			mouseJointDef.maxForce = 1000.0f * projectile.body.getMass();

			mouseJoint = (MouseJoint) physics.getWorld().createJoint(mouseJointDef);
			projectile.body.setAwake(true);
		}
		if (dragging) {
			mouseJoint.setTarget(target.set(temp.x, temp.y));
		}
		if (released) {
			dragging = false;
			released = false;
			if (mouseJoint != null) {
				physics.getWorld().destroyJoint(mouseJoint);
				mouseJoint = null;
			}
			if (slinging.joint != null) {
				dst.set(slinging.joint.getAnchorA()).sub(slinging.joint.getAnchorB());
				physics.getWorld().destroyJoint(slinging.joint);
				e.edit().remove(Slinging.class);

				projectile.body.applyLinearImpulse(dst.scl(5), projectile.body.getWorldCenter(), true);
			}
		}
	}

	Vector3 temp = new Vector3();
	boolean dragging;
	QueryCallback callback = new QueryCallback() {
		@Override public boolean reportFixture (Fixture fixture) {
			Body body = fixture.getBody();
			if (body == targetBody && fixture.testPoint(temp.x, temp.y)) {
				grabbed = true;
				return false;
			}
			return true;
		}
	};

	private MouseJoint mouseJoint;
	@Override public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		if (button != com.badlogic.gdx.Input.Buttons.LEFT) return false;
		gameCamera.unproject(temp.set(screenX, screenY, 0));
		return true;
	}

	Vector2 target = new Vector2();
	@Override public boolean touchDragged (int screenX, int screenY, int pointer) {
		if (!dragging) return false;
		gameCamera.unproject(temp.set(screenX, screenY, 0));
		return true;
	}

	Vector2 dst = new Vector2();
	@Override public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		if (button != com.badlogic.gdx.Input.Buttons.LEFT || !dragging) return false;
		gameCamera.unproject(temp.set(screenX, screenY, 0));
		released = true;
		return false;
	}

	@Override public int priority () {
		return 0;
	}

	@Override public boolean keyDown (int keycode) {
		return false;
	}

	@Override public boolean keyUp (int keycode) {
		return false;
	}

	@Override public boolean keyTyped (char character) {
		return false;
	}

	@Override public boolean mouseMoved (int screenX, int screenY) {
		return false;
	}

	@Override public boolean scrolled (int amount) {
		return false;
	}
}
