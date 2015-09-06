package io.piotrjastrzebski.ecsclones.restrainingofbob.components;

import com.artemis.PooledComponent;

/**
 * Created by PiotrJ on 26/08/15.
 */
public class Health extends PooledComponent {
	public float hp;

	@Override protected void reset () {
		hp = 0;
	}
}
