package io.piotrjastrzebski.ecsclones.restrainingofbob.components;

import com.artemis.PooledComponent;
import io.piotrjastrzebski.ecsclones.restrainingofbob.utils.Direction;

/**
 * Created by PiotrJ on 26/08/15.
 */
public class MoveFacing extends PooledComponent {
	public Direction dir;

	public MoveFacing () {
		reset();
	}

	public void set (float rot) {
		dir = Direction.of(rot);
	}

	@Override protected void reset () {
		dir = Direction.UP;
	}
}
