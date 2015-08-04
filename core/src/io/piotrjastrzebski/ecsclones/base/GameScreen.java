package io.piotrjastrzebski.ecsclones.base;

import com.badlogic.gdx.Input;
import io.piotrjastrzebski.ecsclones.ECSGame;

/**
 * Base screen class for games
 *
 * Created by PiotrJ on 04/08/15.
 */
public abstract class GameScreen extends BaseScreen {
	public GameScreen (ECSGame game) {
		super(game);
	}

	@Override public boolean keyDown (int keycode) {
		if (keycode == Input.Keys.ESCAPE) {
			game.showMainMenu();
		}
		return super.keyDown(keycode);
	}
}
