package io.piotrjastrzebski.ecsclones.restrainingofbob;

import com.artemis.*;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import io.piotrjastrzebski.ecsclones.ECSGame;
import io.piotrjastrzebski.ecsclones.base.GameScreen;
import io.piotrjastrzebski.ecsclones.flapper.WorldGenerator;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.*;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.RemoveAfter;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.*;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.rendering.DebugTint;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.*;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.*;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.physics.*;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.rendering.Box2dDebugRenderer;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.rendering.DebugRenderer;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.rendering.HealthRenderer;

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

		config.setSystem(new Deleter());
		config.setSystem(new Remover());

		config.setSystem(new PlayerSpawner());
		config.setSystem(new MonsterSpawner());

		config.setManager(new BWanderer());
		config.setManager(new BEvader());
		config.setManager(new BPursuer());
		config.setManager(new Meleer());

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
		config.setSystem(new HealthRenderer());
		config.setSystem(new DebugRenderer());
		config.setSystem(new ShooterSystem());
		config.setSystem(new FacingSystem());
//		config.setSystem(new PlayerFacingSystem());
	}

	@Override protected void postInit () {

	}
}
