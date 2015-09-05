package io.piotrjastrzebski.ecsclones.restrainingofbob.processors;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import io.piotrjastrzebski.ecsclones.base.util.Input;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.*;

/**
 * Created by PiotrJ on 29/08/15.
 */
@Wire
public class MoveController extends EntityProcessingSystem implements Input, InputProcessor {
	protected ComponentMapper<Mover> mMover;
	protected ComponentMapper<Transform> mTransform;

	public MoveController () {
		super(Aspect.all(Transform.class, Player.class, Mover.class).exclude(Stunned.class));
	}

	int moveX = 0;
	int moveY = 0;
	Vector2 imp = new Vector2();
	@Override protected void process (Entity e) {
		Mover mover = mMover.get(e);
		Transform transform = mTransform.get(e);
		if (moveX > 0) { // right
			imp.x = mover.maxLinearImp;
			transform.rot = Facing.Direction.RIGHT.angle;
		} else if (moveX < 0) { // left
			imp.x = -mover.maxLinearImp;
			transform.rot = Facing.Direction.LEFT.angle;
		} else {
			imp.x = 0;
		}
		if (moveY > 0) { // up
			imp.y = mover.maxLinearImp;
			transform.rot = Facing.Direction.UP.angle;
		} else if (moveY < 0) { // down
			imp.y = -mover.maxLinearImp;
			transform.rot = Facing.Direction.DOWN.angle;
		} else {
			imp.y = 0;
		}
		imp.limit(mover.maxLinearImp);
		mover.linearImp.set(imp);
	}

	@Override public boolean keyDown (int keycode) {
		switch (keycode) {
		case Keys.W:
		case Keys.UP:
			moveY++;
			return true;
		case Keys.S:
		case Keys.DOWN:
			moveY--;
			return true;
		case Keys.A:
		case Keys.LEFT:
			moveX--;
			return true;
		case Keys.D:
		case Keys.RIGHT:
			moveX++;
			return true;
		}
		return false;
	}

	@Override public boolean keyUp (int keycode) {
		switch (keycode) {
		case Keys.W:
		case Keys.UP:
			moveY--;
			return true;
		case Keys.S:
		case Keys.DOWN:
			moveY++;
			return true;
		case Keys.A:
		case Keys.LEFT:
			moveX++;
			return true;
		case Keys.D:
		case Keys.RIGHT:
			moveX--;
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
		return 1;
	}

	@Override public InputProcessor get () {
		return this;
	}

}
