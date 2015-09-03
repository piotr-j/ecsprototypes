package io.piotrjastrzebski.ecsclones.restrainingofbob.processors;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityEdit;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.physics.box2d.BodyDef;
import io.piotrjastrzebski.ecsclones.base.util.Input;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.*;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.PBodyDef;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.PCircle;

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

	public ShooterSystem () {
		super(Aspect.all(Transform.class, Shooter.class).exclude(Stunned.class));
	}

	@Override protected void process (Entity e) {
		Shooter shooter = mShooter.get(e);
		shooter.timer -= world.delta;
		if (mShoot.has(e)) {
			if (shooter.timer <= 0) {
				shooter.timer = shooter.delay;
				createProjectile(e, shooter.dmg);
			}
		}

		e.edit().remove(Shoot.class);
	}

	private void createProjectile (Entity e, float dmg) {
		Entity p = world.createEntity();
		EntityEdit pe = p.edit();

		Transform trans = pe.create(Transform.class);
		trans.set(mTransform.get(e));
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
		bodyDef.def.linearVelocity.set(1, 0).setAngle(trans.rot).scl(5);
		bodyDef.categoryBits = 0x0002;
		PCircle pCircle = pe.create(PCircle.class);
		pCircle.setSize(0.25f);
	}

}
