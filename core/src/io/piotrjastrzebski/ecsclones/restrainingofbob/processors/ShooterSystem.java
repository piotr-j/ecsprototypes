package io.piotrjastrzebski.ecsclones.restrainingofbob.processors;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityEdit;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.*;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.RemoveAfter;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.PBodyDef;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.PCircle;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.rendering.DebugTint;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.physics.Physics;

/**
 * Created by PiotrJ on 29/08/15.
 */
@Wire
public class ShooterSystem extends EntityProcessingSystem {
	private final static String TAG = ShooterSystem.class.getSimpleName();

	protected ComponentMapper<Shoot> mShoot;
	protected ComponentMapper<Shooter> mShooter;
	protected ComponentMapper<Transform> mTransform;
	protected ComponentMapper<CircleBounds> mCircleBounds;
	protected ComponentMapper<RectBounds> mRectBounds;
	protected ComponentMapper<AimFacing> mFacing;
	protected ComponentMapper<Projectile> mProjectile;
	protected ComponentMapper<Velocity> mVelocity;

	public ShooterSystem () {
		super(Aspect.all(Transform.class, Shooter.class, AimFacing.class, Velocity.class).exclude(Stunned.class));
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

	@Override protected void process (Entity e) {
		Shooter shooter = mShooter.get(e);
		shooter.timer -= world.delta;
		if (mShoot.has(e)) {
			if (shooter.timer <= 0) {
				shooter.timer = shooter.delay;
				createProjectile(e, shooter);
			}
		}

		e.edit().remove(Shoot.class);
	}

	private void createProjectile (Entity e, Shooter shooter) {
		Entity p = world.createEntity();
		EntityEdit pe = p.edit();

		PCircle pCircle = pe.create(PCircle.class);
		pCircle.setSize(0.25f);

		Transform trans = pe.create(Transform.class);
		trans.set(mTransform.get(e));
		trans.pos.sub(pCircle.radius, pCircle.radius);
		if (mRectBounds.has(e)) {
			RectBounds rectBounds = mRectBounds.get(e);
			trans.pos.x += rectBounds.width / 2;
			trans.pos.y += rectBounds.height / 2;
		} else if (mCircleBounds.has(e)) {
			CircleBounds circleBounds = mCircleBounds.get(e);
			trans.pos.x += circleBounds.radius;
			trans.pos.y += circleBounds.radius;
		}

		PBodyDef bodyDef = pe.create(PBodyDef.class);
		bodyDef.type(BodyDef.BodyType.DynamicBody);
		bodyDef.restitution = .25f;
		bodyDef.friction = .25f;
		bodyDef.density = 1;
		AimFacing facing = mFacing.get(e);
		Velocity vel = mVelocity.get(e);
		toAngle(bodyDef.def.linearVelocity, facing.dir.angle)
			.scl(MathUtils
				.random(shooter.vel - shooter.vel * shooter.velSpread, shooter.vel + shooter.vel * shooter.velSpread))
				.add(vel.vel.x * shooter.srcVelMult, vel.vel.y * shooter.srcVelMult);

		bodyDef.categoryBits = Physics.CAT_PROJECTILE;
		bodyDef.maskBits = Physics.MASK_PROJECTILE;

		bodyDef.userData = new Physics.UserData(p) {
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
			Gdx.app.log(TAG, "Default alive time for shooter = " + e.id);
		}
		pe.create(RemoveAfter.class).setDelay(alive);
	}

	protected void onContact(Physics.UserData src, Physics.UserData other) {
		Projectile projectile = mProjectile.get(src.entity);
		Entity hit = world.getEntity(other.entity);
		HitBy hitBy = hit.edit().create(HitBy.class);
		hitBy.dmg = projectile.dmg;
		world.deleteEntity(src.entity);
	}
}
