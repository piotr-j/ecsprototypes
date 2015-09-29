package io.piotrjastrzebski.ecsclones.restrainingofbob;

import com.artemis.*;
import com.artemis.managers.TagManager;
import io.piotrjastrzebski.ecsclones.ECSGame;
import io.piotrjastrzebski.ecsclones.base.GameScreen;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.*;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.*;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.physics.*;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.rendering.Box2dDebugRenderer;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.rendering.DebugRenderer;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.rendering.HealthRenderer;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.rendering.MeleeRangeRenderer;

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

		config.setSystem(new PlayerSpawner());
		config.setSystem(new MonsterSpawner());

		config.setManager(new BWanderer());
		config.setManager(new BEvader());
		config.setManager(new BPursuer());
		config.setManager(new MonsterMelee());
		config.setManager(new MonsterShooter());

		config.setSystem(new BTreeLoader());
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
		config.setSystem(new BrainUpdateSystem());
		config.setSystem(new DeathSystem());
		config.setSystem(new DeathBodySystem());
		config.setSystem(new PhysicsMover());
		config.setSystem(new CircleBoundsUpdater());
		config.setSystem(new RectBoundsUpdater());
		config.setSystem(new CameraFollower());
		config.setSystem(new Box2dDebugRenderer());
		config.setSystem(new HealthRenderer());
		config.setSystem(new DebugRenderer());
		config.setSystem(new MeleeRangeRenderer());
		config.setSystem(new ShooterSystem());
		config.setSystem(new FacingSystem());
		config.setSystem(new Finder());
		config.setSystem(new BTreeUpdater());

		config.setSystem(new Remover());
		config.setSystem(new Deleter());
//		config.setSystem(new PlayerFacingSystem());
	}

	@Override protected void postInit () {

	}
}
