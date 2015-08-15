package io.piotrjastrzebski.ecsclones.slinger.components;

import com.artemis.PooledComponent;

/**
 * Created by EvilEntity on 15/08/2015.
 */
public class Radius extends PooledComponent {
	public float radius;

	public Radius set (float radius) {
		this.radius = radius;
		return this;
	}

	@Override protected void reset () {
		set(0.5f);
	}

	public Radius diameter (float diameter) {
		radius = diameter / 2;
		return this;
	}
}
