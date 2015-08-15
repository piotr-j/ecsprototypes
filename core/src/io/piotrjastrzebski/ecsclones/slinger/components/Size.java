package io.piotrjastrzebski.ecsclones.slinger.components;

import com.artemis.PooledComponent;

/**
 * Created by EvilEntity on 15/08/2015.
 */
public class Size extends PooledComponent {
	public float width, height;

	@Override protected void reset () {
		set(1, 1);
	}

	public Size set (float width, float height) {
		this.width = width;
		this.height = height;
		return this;
	}
}
