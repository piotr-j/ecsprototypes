package io.piotrjastrzebski.ecsclones.base.components;

import com.artemis.PooledComponent;
import com.artemis.utils.IntBag;

/**
 * Created by PiotrJ on 04/08/15.
 */
public class Collided extends PooledComponent {
	public IntBag with = new IntBag();
	@Override protected void reset () {
		with.clear();
	}
}
