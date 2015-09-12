package io.piotrjastrzebski.ecsclones.restrainingofbob.processors;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import io.piotrjastrzebski.ecsclones.base.util.Input;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.*;
import io.piotrjastrzebski.ecsclones.restrainingofbob.utils.Direction;

/**
 * Created by PiotrJ on 29/08/15.
 */
@Wire
public class ShootController extends EntityProcessingSystem implements Input, InputProcessor {
	protected ComponentMapper<AimFacing> mAimFacing;

	public ShootController () {
		super(Aspect.all(Player.class, Shooter.class, AimFacing.class).exclude(Stunned.class, Dead.class));
	}

	int shootX = 0;
	int shootY = 0;

	@Override protected void process (Entity e) {
		// TODO we want to aim in latest direction
		AimFacing aimFacing = mAimFacing.get(e);
		if (shootX > 0) {
			aimFacing.dir = Direction.RIGHT;
		} else if (shootX < 0) {
			aimFacing.dir = Direction.LEFT;
		} else if (shootY > 0) {
			aimFacing.dir = Direction.UP;
		} else if (shootY < 0) {
			aimFacing.dir = Direction.DOWN;
		}
		if (shootX != 0 || shootY != 0) {
			e.edit().create(Shoot.class);
		}
	}

	@Override public boolean keyDown (int keycode) {
		switch (keycode) {
		case Keys.UP:
			shootY++;
			return true;
		case Keys.DOWN:
			shootY--;
			return true;
		case Keys.LEFT:
			shootX--;
			return true;
		case Keys.RIGHT:
			shootX++;
			return true;
		}
		return false;
	}

	@Override public boolean keyUp (int keycode) {
		switch (keycode) {
		case Keys.UP:
			shootY--;
			return true;
		case Keys.DOWN:
			shootY++;
			return true;
		case Keys.LEFT:
			shootX++;
			return true;
		case Keys.RIGHT:
			shootX--;
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
