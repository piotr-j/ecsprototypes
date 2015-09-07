package io.piotrjastrzebski.ecsclones.restrainingofbob.components;

import com.artemis.PooledComponent;
import io.piotrjastrzebski.ecsclones.restrainingofbob.utils.Direction;

/**
 * Created by PiotrJ on 26/08/15.
 */
public class AimFacing extends PooledComponent {
	public Direction dir;

	public AimFacing () {
		reset();
	}

	public void set (float rot) {
		dir = Direction.of(rot);
	}

	@Override protected void reset () {
		dir = Direction.UP;
	}
}
