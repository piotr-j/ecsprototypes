package io.piotrjastrzebski.ecsclones.base.components;

import com.artemis.PooledComponent;

/**
 * Created by PiotrJ on 04/08/15.
 */
public class Size extends PooledComponent {
	public float width;
	public float height;

	@Override protected void reset () {
		width = 0;
		height = 0;
	}
}
