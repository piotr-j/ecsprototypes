package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntityEdit;
import com.artemis.EntitySystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import io.piotrjastrzebski.ecsclones.restrainingofbob.RoBScreen;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.*;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.status.Health;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.*;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.rendering.DebugTint;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.physics.Physics;

/**
 * Created by PiotrJ on 31/08/15.
 */
@Wire
public class MonsterSpawner extends EntitySystem {

	public MonsterSpawner () {
		super(Aspect.all(EnemyBrain.class));
	}

	@Override protected void processSystem () {
		int toSpawn = 200 - getSubscription().getEntities().size();
		for (int i = 0; i < toSpawn; i++) {
			spawnEnemy();
		}
	}

	private void spawnEnemy () {
		Entity e = world.createEntity();
		EntityEdit ee = e.edit();

		ee.create(CircleBounds.class).radius(.25f);

		Transform transform = ee.create(Transform.class);
		transform.pos.set(
			MathUtils.random(-RoBScreen.VP_WIDTH/3, RoBScreen.VP_WIDTH/3),
			MathUtils.random(-RoBScreen.VP_HEIGHT/3, RoBScreen.VP_HEIGHT/3)
		);

		Mover mover = ee.create(Mover.class);
		mover.maxLinearImp = 2.5f;

		PBodyDef bodyDef = ee.create(PBodyDef.class);
		bodyDef.type(BodyDef.BodyType.DynamicBody);
		bodyDef.def.linearDamping = 5f;
		bodyDef.restitution = .25f;
		bodyDef.friction = .25f;
		bodyDef.density = 1;

		bodyDef.categoryBits = Physics.CAT_ENEMY;
		bodyDef.maskBits = Physics.MASK_ENEMY;

		PCircle pCircle = ee.create(PCircle.class);
		pCircle.setSize(.5f);


		PSteerable physSteerable = ee.create(PSteerable.class);
		physSteerable.setMaxLinearAcceleration(6);
		physSteerable.setMaxLinearSpeed(2);
		physSteerable.setMaxAngularAcceleration(0.5f); // greater than 0 because independent facing is enabled
		physSteerable.setMaxAngularSpeed(5);
		physSteerable.setIndependentFacing(true);
		physSteerable.setBoundingRadius(0.25f);

		EnemyBrain brain = ee.create(EnemyBrain.class);
		brain.minDst2 = 5;
		brain.id = e.getId();
		// TODO need to define this in some reasonable way
		switch (MathUtils.random(9)) {
		default:
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
			brain.treePath = "rob/ai/monster/melee.tree";
			ee.create(DebugTint.class).setBase(Color.YELLOW);
			break;
		case 7:
		case 8:
			// TODO charged ranged attack that can be interrupted by hitting the monster
			brain.treePath = "rob/ai/monster/ranged.tree";
			Shooter shooter = ee.create(Shooter.class);
			shooter.dmg = 2;
			shooter.alive = 3;
			shooter.delay = 1;
			shooter.vel = 7;
			shooter.collisionCategory = Physics.CAT_PROJECTILE_E;
			shooter.collisionMask = Physics.CAT_PLAYER;
			ee.create(AimDirection.class);
			ee.create(DebugTint.class).setBase(Color.ORANGE);
			break;
		case 9:
			brain.treePath = "rob/ai/monster/healer.tree";
			ee.create(DebugTint.class).setBase(Color.GREEN);
			break;
		}

		ee.create(Velocity.class);

		ee.create(Health.class).hp(3);
		ee.create(MoveFacing.class);
	}
}
