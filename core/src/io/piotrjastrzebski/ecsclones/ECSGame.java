package io.piotrjastrzebski.ecsclones;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Constructor;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.layout.GridGroup;
import com.kotcrab.vis.ui.widget.VisTextButton;
import io.piotrjastrzebski.ecsclones.base.BaseScreen;
import io.piotrjastrzebski.ecsclones.base.GameScreen;
import io.piotrjastrzebski.ecsclones.base.PlatformBridge;
import io.piotrjastrzebski.ecsclones.flapper.FlapperScreen;
import io.piotrjastrzebski.ecsclones.restrainingofbob.RoBScreen;
import io.piotrjastrzebski.ecsclones.slinger.SlingerScreen;

public class ECSGame extends Game {
	SpriteBatch batch;
	ShapeRenderer renderer;
	MainMenuScreen menuScreen;
	PlatformBridge bridge;

	public ECSGame (PlatformBridge bridge) {
		this.bridge = bridge;
	}

	@Override
	public void create () {
		boolean highRes = Math.max(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) > 1980;
		if (highRes || bridge.getPixelScaleFactor() > 1.5f) {
			VisUI.load(VisUI.SkinScale.X2);
		} else {
			VisUI.load(VisUI.SkinScale.X1);
		}
		batch = new SpriteBatch();
		renderer = new ShapeRenderer();
		menuScreen = new MainMenuScreen(this);
		showMainMenu();
	}

	@Override public void dispose () {
		super.dispose();
		batch.dispose();
		renderer.dispose();
		VisUI.dispose();
	}

	public void showMainMenu () {
		setScreen(menuScreen);
	}

	public SpriteBatch getBatch () {
		return batch;
	}

	public ShapeRenderer getRenderer () {
		return renderer;
	}

	private class MainMenuScreen extends BaseScreen {
		GridGroup group;
		public MainMenuScreen (ECSGame game) {
			super(game);
			group = new GridGroup();
			button(FlapperScreen.class);
//			button(Roguer.class);
			button(SlingerScreen.class);
			button(RoBScreen.class);
//			button(Thirder.class);
			root.add(group).expand().fill();
		}

		private void button (final Class<? extends GameScreen> aClass) {
			VisTextButton button = new VisTextButton("Run " + aClass.getSimpleName());
			button.addListener(new ClickListener() {
				@Override public void clicked (InputEvent event, float x, float y) {
					run(aClass);
				}
			});
			group.addActor(button);
		}

		private void run (Class<? extends GameScreen> aClass) {
			try {
				Constructor constructor = ClassReflection.getConstructor(aClass, ECSGame.class);
				setScreen((GameScreen)constructor.newInstance(game));
			} catch (ReflectionException e) {
				e.printStackTrace();
			}
		}

		@Override public void show () {
			Gdx.input.setInputProcessor(multiplexer);
		}

		@Override public void render (float delta) {
			super.render(delta);
			stage.act(delta);
			stage.draw();
		}

		@Override public void dispose () {
			// don't do anything in on dispose, we will reuse this screen
		}
	}
}
