package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.input;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import io.piotrjastrzebski.ecsclones.base.util.Input;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.*;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.status.Dead;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.status.Stunned;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.tags.Shoot;
import io.piotrjastrzebski.ecsclones.restrainingofbob.utils.Direction;

/**
 * Created by PiotrJ on 29/08/15.
 */
@Wire
public class ShootController extends IteratingSystem implements Input, InputProcessor {
	protected ComponentMapper<AimDirection> mAimFacing;
	protected ComponentMapper<Shoot> mShoot;

	public ShootController () {
		super(Aspect.all(Player.class, Shooter.class, AimDirection.class).exclude(Stunned.class, Dead.class));
	}

	int shootX = 0;
	int shootY = 0;

	@Override protected void process (int eid) {
		// TODO we want to aim in latest direction
		AimDirection aimDirection = mAimFacing.get(eid);
		if (shootX > 0) {
			aimDirection.angle = Direction.RIGHT.angle;
		} else if (shootX < 0) {
			aimDirection.angle = Direction.LEFT.angle;
		} else if (shootY > 0) {
			aimDirection.angle = Direction.UP.angle;
		} else if (shootY < 0) {
			aimDirection.angle = Direction.DOWN.angle;
		}
		if (shootX != 0 || shootY != 0) {
			mShoot.create(eid);
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
