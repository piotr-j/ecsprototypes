package io.piotrjastrzebski.ecsclones.restrainingofbob;

import com.artemis.Entity;
import com.artemis.EntityEdit;
import com.artemis.WorldConfiguration;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import io.piotrjastrzebski.ecsclones.ECSGame;
import io.piotrjastrzebski.ecsclones.base.GameScreen;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.*;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.*;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.rendering.DebugTint;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.*;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.*;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.physics.*;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.rendering.Box2dDebugRenderer;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.rendering.DebugRenderer;

/**
 * Simple angry birds like in ecs
 * Created by EvilEntity on 30/07/2015.
 */
public class RoBScreen extends GameScreen {

	public RoBScreen (ECSGame game) {
		super(game);

	}

	@Override protected void preInit (WorldConfiguration config) {
		config.setManager(new TagManager());

		config.setSystem(new Remover());

		config.setManager(new BWanderer());
		config.setManager(new BEvader());
		config.setManager(new BPursuer());

		config.setSystem(new BTreeLoader());
		config.setSystem(new BTreeUpdater());
		config.setSystem(new Finder());
		config.setManager(new PhysicsContacts());
		config.setSystem(new Physics());
		config.setSystem(new PhysicsCleaner());
		config.setSystem(new Steering());
		config.setSystem(new TransformUpdater());
		config.setSystem(new VelocityUpdater());
		config.setSystem(new PBodyBuilder());
		config.setSystem(new MoveController());
		config.setSystem(new ShootController());
		config.setSystem(new HitBySystem());
		config.setSystem(new DeathSystem());
		config.setSystem(new DeathBodySystem());
		config.setSystem(new PhysicsMover());
		config.setSystem(new CircleBoundsUpdater());
		config.setSystem(new RectBoundsUpdater());
		config.setSystem(new CameraFollower());
		config.setSystem(new Box2dDebugRenderer());
		config.setSystem(new DebugRenderer());
		config.setSystem(new ShooterSystem());
		config.setSystem(new FacingSystem());
//		config.setSystem(new PlayerFacingSystem());
	}

	@Override protected void postInit () {
		createPlayer();

		for (int i = 0; i < 200; i++) {
			createEnemy();
		}
	}

	private void createPlayer () {
		Entity player = world.createEntity();
		world.getManager(TagManager.class).register("player", player);
		EntityEdit edit = player.edit();
		edit.create(Player.class).name = "Player 1";
		Transform transform = edit.create(Transform.class);
		transform.pos.set(0, 0);

		edit.create(DebugTint.class).color.set(Color.CYAN);
		edit.create(CircleBounds.class).radius(.5f);

		Shooter shooter = edit.create(Shooter.class);
		// how often can it shoot
		shooter.delay = 0.25f;
		// dmg of the projectile
		shooter.dmg = 2;
		// +- fraction of vel
		shooter.dmgSpread = .5f;
		// +- fraction of vel
//		shooter.dmgSpreadMult = 0.1f;
		shooter.vel = 10;
		// +- fraction of vel
		shooter.velSpread = 0.1f;
		// alive time in seconts for projectile
		shooter.alive = 1f;
		// +- fraction of alive
		shooter.aliveSpread = 0.1f;
		// fraction of src vel to add to projectile
		shooter.srcVelMult = 0.5f;

		Mover mover = edit.create(Mover.class);
		mover.maxLinearImp = 1f;

		PBodyDef bodyDef = edit.create(PBodyDef.class);
		bodyDef.type(BodyDef.BodyType.DynamicBody);
		bodyDef.def.linearDamping = mover.maxLinearImp * 10f;
		bodyDef.def.fixedRotation = true;
		bodyDef.restitution = .25f;
		bodyDef.friction = .5f;
		bodyDef.density = 1;

		bodyDef.categoryBits = Physics.CAT_PLAYER;
		bodyDef.maskBits = Physics.MASK_PLAYER;

		PCircle pCircle = edit.create(PCircle.class);
		pCircle.setSize(1f);

		CamFollow follow = edit.create(CamFollow.class);
		follow.offsetX = 0.5f;
		follow.offsetY = 0.5f;

		PSteerable physSteerable = edit.create(PSteerable.class);
		physSteerable.setMaxLinearAcceleration(4);
		physSteerable.setMaxLinearSpeed(1);
		physSteerable.setMaxAngularAcceleration(0.5f); // greater than 0 because independent facing is enabled
		physSteerable.setMaxAngularSpeed(5);
		physSteerable.setIndependentFacing(true);
		physSteerable.setBoundingRadius(0.25f);

		edit.create(MoveFacing.class);
		edit.create(AimFacing.class);
		edit.create(Velocity.class);
	}

	private void createEnemy () {
		Entity e = world.createEntity();
		EntityEdit ee = e.edit();

		ee.create(DebugTint.class).color.set(Color.YELLOW);
		ee.create(CircleBounds.class).radius(.25f);

		Transform transform = ee.create(Transform.class);
		transform.pos.set(MathUtils.random(-VP_WIDTH, VP_WIDTH), MathUtils.random(-VP_HEIGHT, VP_HEIGHT));

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
		brain.hp = 10;
		brain.maxHP = 10;
		brain.id = e.id;
		brain.treePath = "rob/ai/monster.tree";

		ee.create(Velocity.class);

		ee.create(Health.class).hp = 3;
		ee.create(MoveFacing.class);
//		edit.create(MoveFacing.class);
	}
}
