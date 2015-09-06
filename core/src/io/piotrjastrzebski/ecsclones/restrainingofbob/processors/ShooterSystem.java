package io.piotrjastrzebski.ecsclones.restrainingofbob.processors;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityEdit;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import io.piotrjastrzebski.ecsclones.base.util.Input;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.*;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.RemoveAfter;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.PBodyDef;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.PCircle;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.physics.Physics;

/**
 * Created by PiotrJ on 29/08/15.
 */
@Wire
public class ShooterSystem extends EntityProcessingSystem {
	protected ComponentMapper<Shoot> mShoot;
	protected ComponentMapper<Shooter> mShooter;
	protected ComponentMapper<Transform> mTransform;
	protected ComponentMapper<CircleBounds> mCircleBounds;
	protected ComponentMapper<RectBounds> mRectBounds;
	protected ComponentMapper<Facing> mFacing;
	protected ComponentMapper<Projectile> mProjectile;

	public ShooterSystem () {
		super(Aspect.all(Transform.class, Shooter.class, Facing.class).exclude(Stunned.class));
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
		Facing facing = mFacing.get(e);
		float angle = facing.dir.angle + 90;
		toAngle(bodyDef.def.linearVelocity, angle).scl(shooter.vel);

		bodyDef.categoryBits = Physics.CAT_PROJECTILE;
		bodyDef.maskBits = Physics.MASK_PROJECTILE;

		bodyDef.userData = new Physics.UserData(p) {
			@Override public void onContact (Physics.UserData other) {
				ShooterSystem.this.onContact(this, other);
			}
		};

		Projectile projectile = pe.create(Projectile.class);
		projectile.dmg = shooter.dmg;

		float alive = shooter.alive > 0?shooter.alive:15;
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
