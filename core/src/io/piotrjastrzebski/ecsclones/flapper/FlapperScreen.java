package io.piotrjastrzebski.ecsclones.flapper;

import com.artemis.WorldConfiguration;
import io.piotrjastrzebski.ecsclones.ECSGame;
import io.piotrjastrzebski.ecsclones.base.GameScreen;

/**
 * Simple flappy bird clone in ecs
 * Created by EvilEntity on 30/07/2015.
 */
public class FlapperScreen extends GameScreen {
	public FlapperScreen (ECSGame game) {
		super(game);
	}

	@Override protected void preInit (WorldConfiguration config) {
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
	}

	@Override protected void postInit () {

	}

	@Override public void render (float delta) {
		super.render(delta);
		stage.act(delta);
		stage.draw();
	}
}
