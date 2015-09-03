package io.piotrjastrzebski.ecsclones.restrainingofbob.processors;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import io.piotrjastrzebski.ecsclones.base.util.Input;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.Player;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.Shoot;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.Shooter;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.Stunned;

/**
 * Created by PiotrJ on 29/08/15.
 */
@Wire
public class ShootController extends EntityProcessingSystem implements Input, InputProcessor {
	protected ComponentMapper<Shooter> mShooter;

	public ShootController () {
		super(Aspect.all(Player.class, Shooter.class).exclude(Stunned.class));
	}

	int shoot = 0;

	@Override protected void process (Entity e) {
		if (shoot > 0) {
			e.edit().create(Shoot.class);
		}
	}

	@Override public boolean keyDown (int keycode) {
		switch (keycode) {
		case Keys.SPACE:
			shoot++;
			return true;
		}
		return false;
	}

	@Override public boolean keyUp (int keycode) {
		switch (keycode) {
		case Keys.SPACE:
			shoot--;
			return true;
		}
		return false;
	}

	@Override public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override public boolean touchDragged (int screenX, int screenY, int pointer) {
		return false;
	}

	@Override public boolean mouseMoved (int screenX, int screenY) {
		return false;
	}

	@Override public boolean scrolled (int amount) {
		return false;
	}

	@Override public boolean keyTyped (char character) {
		return false;
	}

	@Override public int priority () {
		return 2;
	}

	@Override public InputProcessor get () {
		return this;
	}

}
