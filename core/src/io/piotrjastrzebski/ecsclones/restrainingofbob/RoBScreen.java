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
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.physics.PBodyBuilder;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.physics.Physics;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.physics.PhysicsContacts;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.physics.PhysicsMover;
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

		config.setManager(new BWanderer());
		config.setManager(new BEvader());
		config.setManager(new BPursuer());

		config.setSystem(new BTreeLoader());
		config.setSystem(new BTreeUpdater());
		config.setSystem(new Finder());
		config.setManager(new PhysicsContacts());
		config.setSystem(new Physics());
		config.setSystem(new Steering());
		config.setSystem(new TransformUpdater());
		config.setSystem(new PBodyBuilder());
		config.setSystem(new MoveController());
		config.setSystem(new ShootController());
		config.setSystem(new PhysicsMover());
		config.setSystem(new CircleBoundsUpdater());
		config.setSystem(new RectBoundsUpdater());
		config.setSystem(new CameraFollower());
		config.setSystem(new Box2dDebugRenderer());
		config.setSystem(new DebugRenderer());
	}

	@Override protected void postInit () {
		// TODO create player
		PSteerable player = createPlayer();

		// TODO create a bunch of enemies
		for (int i = 0; i < 50; i++) {
			createEnemy(player);
		}
	}

	private PSteerable createPlayer () {
		Entity player = world.createEntity();
		world.getManager(TagManager.class).register("player", player);
		EntityEdit edit = player.edit();
		edit.create(Player.class).name = "Player 1";
		Transform transform = edit.create(Transform.class);
		transform.pos.set(0, 0);

		edit.create(DebugTint.class).color.set(Color.CYAN);
		edit.create(CircleBounds.class).radius(.5f);

		Shooter shooter = edit.create(Shooter.class);

		Mover mover = edit.create(Mover.class);
		mover.maxLinearImp = 5;

		PBodyDef bodyDef = edit.create(PBodyDef.class);
		bodyDef.type(BodyDef.BodyType.DynamicBody);
		bodyDef.def.linearDamping = 25f;
		bodyDef.def.fixedRotation = true;
		bodyDef.restitution = .25f;
		bodyDef.friction = .25f;
		bodyDef.density = 1;

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
		return physSteerable;
	}

	private void createEnemy (PSteerable player) {
		Entity e = world.createEntity();
		EntityEdit ee = e.edit();

		ee.create(DebugTint.class).color.set(Color.YELLOW);
		ee.create(CircleBounds.class).radius(.25f);

		Transform transform = ee.create(Transform.class);
		transform.pos.set(MathUtils.random(-18, 17), MathUtils.random(-10, 9));

		Mover mover = ee.create(Mover.class);
		mover.maxLinearImp = 2.5f;

		PBodyDef bodyDef = ee.create(PBodyDef.class);
		bodyDef.type(BodyDef.BodyType.DynamicBody);
		bodyDef.def.linearDamping = 5f;
		bodyDef.restitution = .25f;
		bodyDef.friction = .25f;
		bodyDef.density = 1;

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
	}
}
