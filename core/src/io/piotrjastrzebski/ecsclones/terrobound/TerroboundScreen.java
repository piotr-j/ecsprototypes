package io.piotrjastrzebski.ecsclones.terrobound;

import com.artemis.EntityEdit;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;
import io.piotrjastrzebski.ecsclones.ECSGame;
import io.piotrjastrzebski.ecsclones.base.GameScreen;
import io.piotrjastrzebski.ecsclones.slinger.components.Level;
import io.piotrjastrzebski.ecsclones.slinger.components.WorldDef;
import io.piotrjastrzebski.ecsclones.slinger.systems.Box2dRenderer;
import io.piotrjastrzebski.ecsclones.slinger.systems.DebugRenderer;
import io.piotrjastrzebski.ecsclones.slinger.systems.GUISystem;
import io.piotrjastrzebski.ecsclones.slinger.systems.LevelLoader;
import io.piotrjastrzebski.ecsclones.slinger.systems.physics.*;
import io.piotrjastrzebski.ecsclones.slinger.util.Input;
import io.piotrjastrzebski.ecsclones.slinger.util.Resizing;

import java.util.Comparator;

/**
 * Simple angry birds like in ecs
 * Created by EvilEntity on 30/07/2015.
 */
public class TerroboundScreen extends GameScreen {
	public static final String WIRE_GUI_CAM = "gui-cam";
	public static final String WIRE_GUI_VP = "gui-vp";
	public static final String WIRE_GAME_CAM = "game-cam";
	public static final String WIRE_GAME_VP = "game-vp";

	World world;

	public TerroboundScreen (ECSGame game) {
		super(game);
		WorldConfiguration config = new WorldConfiguration();
		config.register(WIRE_GUI_CAM, guiCamera);
		config.register(WIRE_GUI_VP, guiViewport);
		config.register(WIRE_GAME_CAM, gameCamera);
		config.register(WIRE_GAME_VP, gameViewport);
		config.register(batch);
		config.register(renderer);
		config.register(stage);


		world = new World(config);

		Array<Input> inputs = new Array<>();
		input(inputs, world.getManagers());
		input(inputs, world.getSystems());
		inputs.sort(new Comparator<Input>() {
			@Override public int compare (Input o1, Input o2) {
				return o1.priority() - o2.priority();
			}
		});
		for (Input input : inputs) {
			multiplexer.addProcessor(input);
		}
	}

	@Override public void render (float delta) {
		super.render(delta);
		world.delta = delta;
		world.process();
	}

	@Override public void resize (int width, int height) {
		super.resize(width, height);
		resize(world.getSystems(), width, height);
		resize(world.getManagers(), width, height);
	}

	private void resize(ImmutableBag bag, int width, int height) {
		for (Object object : bag) {
			if (object instanceof Resizing) {
				((Resizing)object).resize(width, height);
			}
		}
	}

	private void input(Array<Input> inputs, ImmutableBag bag) {
		for (Object object : bag) {
			if (object instanceof InputProcessor) {
				inputs.add((Input)object);
			}
		}
	}

	@Override public void dispose () {
		super.dispose();
		world.dispose();
	}
}
