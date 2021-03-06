package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityEdit;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.*;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.status.HitBy;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.status.Stunned;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.*;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.rendering.DebugTint;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.tags.Shoot;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.physics.Physics;

/**
 * Created by PiotrJ on 29/08/15.
 */
@Wire
public class ShooterSystem extends IteratingSystem {
	private final static String TAG = ShooterSystem.class.getSimpleName();

	protected ComponentMapper<Shoot> mShoot;
	protected ComponentMapper<Shooter> mShooter;
	protected ComponentMapper<Transform> mTransform;
	protected ComponentMapper<CircleBounds> mCircleBounds;
	protected ComponentMapper<RectBounds> mRectBounds;
	protected ComponentMapper<AimDirection> mFacing;
	protected ComponentMapper<Projectile> mProjectile;
	protected ComponentMapper<Velocity> mVelocity;

	public ShooterSystem () {
		super(Aspect.all(Transform.class, Shooter.class, AimDirection.class, Velocity.class).exclude(Stunned.class));
	}

	private Vector2 toAngle(Vector2 v, float angle) {
		v.set(1, 0).setAngle(angle);
		if (MathUtils.isZero(v.x, 0.001f)) {
			v.x = 0;
		}
		if (MathUtils.isZero(v.y, 0.001f)) {
			v.y = 0;
		}
		return v;
	}

	@Override protected void process (int eid) {
		Shooter shooter = mShooter.get(eid);
		shooter.timer -= world.delta;
		if (mShoot.has(eid)) {
			if (shooter.timer <= 0) {
				shooter.timer = shooter.delay;
				createProjectile(eid, shooter);
			}
		}
		mShoot.remove(eid);
	}

	private void createProjectile (int eid, Shooter shooter) {
		Entity p = world.createEntity();
		EntityEdit pe = p.edit();

		PCircle pCircle = pe.create(PCircle.class);
		pCircle.setSize(0.25f);

		Transform trans = pe.create(Transform.class);
		trans.set(mTransform.get(eid));
		trans.pos.sub(pCircle.radius, pCircle.radius);
		if (mRectBounds.has(eid)) {
			RectBounds rectBounds = mRectBounds.get(eid);
			trans.pos.x += rectBounds.width / 2;
			trans.pos.y += rectBounds.height / 2;
		} else if (mCircleBounds.has(eid)) {
			CircleBounds circleBounds = mCircleBounds.get(eid);
			trans.pos.x += circleBounds.radius;
			trans.pos.y += circleBounds.radius;
		}

		PBodyDef bodyDef = pe.create(PBodyDef.class);
		bodyDef.type(BodyDef.BodyType.DynamicBody);
		bodyDef.restitution = .25f;
		bodyDef.friction = .25f;
		bodyDef.density = 1;
		AimDirection facing = mFacing.get(eid);
		Velocity vel = mVelocity.get(eid);
		toAngle(bodyDef.def.linearVelocity, facing.angle)
			.scl(MathUtils.random(shooter.vel - shooter.vel * shooter.velSpread, shooter.vel + shooter.vel * shooter.velSpread))
				.add(vel.vel.x * shooter.srcVelMult, vel.vel.y * shooter.srcVelMult);

		bodyDef.categoryBits = shooter.collisionCategory;
		bodyDef.maskBits = shooter.collisionMask;

		bodyDef.userData = new Physics.UserData(p.getId()) {
			@Override public void onContact (Physics.UserData other) {
				ShooterSystem.this.onContact(this, other);
			}
		};

		Projectile projectile = pe.create(Projectile.class);
		projectile.dmg = MathUtils.random(
			shooter.dmg - shooter.dmg * shooter.dmgSpread,
			shooter.dmg + shooter.dmg * shooter.dmgSpread);

		if (projectile.dmg < 0) projectile.dmg = 0;

		pe.create(DebugTint.class).color.set(Color.GREEN);
		pe.create(CircleBounds.class).radius(pCircle.radius);

		float alive = 1;
		if (shooter.alive > 0) {
			alive = MathUtils.random(
				shooter.alive - shooter.alive * shooter.aliveSpread,
				shooter.alive + shooter.alive * shooter.aliveSpread);
		} else {
			Gdx.app.log(TAG, "Default alive time for shooter = " + eid);
		}
		pe.create(DeleteAfter.class).setDelay(alive);
	}

	protected void onContact(Physics.UserData p, Physics.UserData t) {
		Entity pe = world.getEntity(p.eid);
		if (!pe.isActive()) return;
		Entity hit = world.getEntity(t.eid);
		if (!hit.isActive()) return;

		Projectile projectile = mProjectile.get(p.eid);
		HitBy hitBy = hit.edit().create(HitBy.class);
		hitBy.dmg = projectile.dmg;
		pe.edit().create(DeleteAfter.class);
	}
}
