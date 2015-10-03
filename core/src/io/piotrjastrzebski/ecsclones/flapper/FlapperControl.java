package io.piotrjastrzebski.ecsclones.flapper;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import io.piotrjastrzebski.ecsclones.base.components.Movement;
import io.piotrjastrzebski.ecsclones.flapper.components.Flapper;

/**
 * Created by PiotrJ on 04/08/15.
 */
@Wire
public class FlapperControl extends IteratingSystem implements InputProcessor {
	private ComponentMapper<Flapper> mFlapper;
	private ComponentMapper<Movement> mMovement;

	public FlapperControl () {
		super(Aspect.all(Flapper.class, Movement.class));
	}

	private boolean jump;
	@Override protected void inserted (int eid) {
		Flapper flapper = mFlapper.get(eid);
		Movement movement = mMovement.get(eid);
		movement.acc.add(flapper.forwardAcc);
	}

	@Override protected void process (int eid) {
		Flapper flapper = mFlapper.get(eid);
		Movement movement = mMovement.get(eid);
		if (jump) {
			jump = false;
			movement.vel.y = 0;
			movement.acc.y = 0;
			movement.vel.add(flapper.jumpAcc);
		}
		// dedicated systems?
//		movement.acc.add(flapper.forwardAcc);
	}

	@Override protected void removed (int eid) {

	}

	@Override public boolean keyDown (int keycode) {
		if (keycode == Input.Keys.SPACE) {
			jump = true;
		}
		return false;
	}

	@Override public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		jump = true;
		return false;
	}

	@Override public boolean keyUp (int keycode) {
		return false;
	}

	@Override public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override public boolean keyTyped (char character) {
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
}
