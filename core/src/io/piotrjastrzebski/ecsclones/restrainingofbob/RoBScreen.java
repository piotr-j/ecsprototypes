package io.piotrjastrzebski.ecsclones.restrainingofbob;

import com.artemis.Entity;
import com.artemis.EntityEdit;
import com.artemis.WorldConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import io.piotrjastrzebski.ecsclones.ECSGame;
import io.piotrjastrzebski.ecsclones.base.GameScreen;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.*;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.PBodyDef;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.PCircle;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.PPolygon;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.PRect;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.rendering.DebugTint;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.*;
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
		config.setManager(new PhysicsContacts());
		config.setSystem(new Physics());
		config.setSystem(new TransformUpdater());
		config.setSystem(new PBodyBuilder());
		config.setSystem(new MoveController());
		config.setSystem(new ShootController());
		config.setSystem(new PhysicsMover());
		config.setSystem(new CircleBoundsUpdater());
		config.setSystem(new RectBoundsUpdater());
		config.setSystem(new Follower());
		config.setSystem(new DebugRenderer());
		config.setSystem(new Box2dDebugRenderer());
	}

	@Override protected void postInit () {
		// TODO create player
		createPlayer();

		// TODO create a bunch of enemies
		for (int i = 0; i < 25; i++) {
			createEnemy();
		}
	}

	private void createPlayer () {
		Entity player = world.createEntity();
		EntityEdit edit = player.edit();
		edit.create(Player.class).name = "Player 1";
		Transform transform = edit.create(Transform.class);
		transform.pos.set(0, 0);

//		CircleBounds circle = edit.create(CircleBounds.class);
//		circle.setSize(1f);

//		RectBounds rectBounds = edit.create(RectBounds.class);
//		rectBounds.setSize(1f);
//		edit.create(DebugTint.class).color.set(Color.GREEN);

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

	}

	private void createEnemy () {
		Entity e = world.createEntity();
		EntityEdit ee = e.edit();

		Transform transform = ee.create(Transform.class);
		transform.pos.set(MathUtils.random(-18, 17), MathUtils.random(-10, 9));

		Mover mover = ee.create(Mover.class);
		mover.maxLinearImp = 2.5f;

		PBodyDef bodyDef = ee.create(PBodyDef.class);
		bodyDef.type(BodyDef.BodyType.DynamicBody);
		bodyDef.def.linearDamping = 5f;
		bodyDef.def.fixedRotation = true;
		bodyDef.restitution = .25f;
		bodyDef.friction = .25f;
		bodyDef.density = 1;

		PCircle pCircle = ee.create(PCircle.class);
		pCircle.setSize(.5f);
	}
}
