package io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic;

import com.artemis.PooledComponent;

/**
 * Created by PiotrJ on 26/08/15.
 */
public class RemoveAfter extends PooledComponent {
	public float delay;
	public float timer;

	public RemoveAfter setDelay (float delay) {
		this.delay = delay;
		return this;
	}

	@Override protected void reset () {
		delay = 0;
		timer = 0;
	}
}
