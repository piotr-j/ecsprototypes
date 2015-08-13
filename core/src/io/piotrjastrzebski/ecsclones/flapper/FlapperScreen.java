package io.piotrjastrzebski.ecsclones.flapper;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import io.piotrjastrzebski.ecsclones.ECSGame;
import io.piotrjastrzebski.ecsclones.base.GameScreen;

/**
 * Simple flappy bird clone in ecs
 * Created by EvilEntity on 30/07/2015.
 */
public class FlapperScreen extends GameScreen {
	World world;
	public FlapperScreen (ECSGame game) {
		super(game);
		WorldConfiguration config = new WorldConfiguration();
		config.register(gameCamera);
		config.register(gameViewport);
		config.register(renderer);
		config.register(batch);

		config.setSystem(new FlapperRespawner());

		config.setSystem(new GravitySystem());
		config.setSystem(new MovementSystem());

		FlapperControl flapperControl = new FlapperControl();
		config.setSystem(flapperControl);
		multiplexer.addProcessor(0, flapperControl);

		config.setSystem(new FlapperFollower());
		config.setSystem(new WorldGenerator());

		config.setSystem(new CollisionFinder());
		config.setSystem(new FlapperCollisionResolver());
		config.setSystem(new CollisionCleaner());

		config.setSystem(new OOBCleaner());

		config.setSystem(new DebugRenderer());


		world = new World(config);

	}

	@Override public void render (float delta) {
		super.render(delta);
		world.delta = delta;
		world.process();
		stage.act(delta);
		stage.draw();
	}

	@Override public void dispose () {
		super.dispose();
		world.dispose();
	}
}
