package io.piotrjastrzebski.ecsclones.slinger;

import com.artemis.EntityEdit;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;
import io.piotrjastrzebski.ecsclones.ECSGame;
import io.piotrjastrzebski.ecsclones.base.GameScreen;
import io.piotrjastrzebski.ecsclones.slinger.components.*;
import io.piotrjastrzebski.ecsclones.slinger.systems.*;
import io.piotrjastrzebski.ecsclones.slinger.systems.physics.*;
import io.piotrjastrzebski.ecsclones.base.util.Input;
import io.piotrjastrzebski.ecsclones.base.util.Resizing;

import java.util.Comparator;

/**
 * Simple angry birds like in ecs
 * Created by EvilEntity on 30/07/2015.
 */
public class SlingerScreen extends GameScreen {
	Physics physics;

	public SlingerScreen (ECSGame game) {
		super(game);
	}

	@Override protected void preInit (WorldConfiguration config) {
		config.setSystem(new PhysicsContacts());
		config.setSystem(physics = new Physics());
		config.setSystem(new LevelLoader());
		config.setSystem(new GroundMaker());
		config.setSystem(new BlockMaker());
		config.setSystem(new SlingMaker());
		config.setSystem(new ProjectileMaker());
		config.setSystem(new TargetMaker());

		config.setSystem(new ProjectileLinker());
		config.setSystem(new ProjectileGrabber());

		config.setSystem(new Box2dRenderer());
		config.setSystem(new DebugRenderer());
		config.setSystem(new GUISystem());
	}

	@Override protected void postInit () {

		EntityEdit b2dWorld = world.createEntity().edit();
		// TODO tweak stuff
		b2dWorld.create(WorldDef.class);
		// process so box2d world is created
		world.process();

		EntityEdit level = world.createEntity().edit();
		// TODO add some stuff
		level.create(Level.class);
	}
}
