package io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic;

import com.artemis.PooledComponent;
import io.piotrjastrzebski.ecsclones.restrainingofbob.utils.Direction;

/**
 * Created by PiotrJ on 26/08/15.
 */
public class AimDirection extends PooledComponent {
	public float angle;

	public AimDirection () {
		reset();
	}

	public void set (float angle) {
		this.angle = angle;
	}

	@Override protected void reset () {
		angle = 0;
	}
}
