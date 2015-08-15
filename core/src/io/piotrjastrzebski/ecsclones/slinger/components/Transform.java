package io.piotrjastrzebski.ecsclones.slinger.components;

import com.artemis.PooledComponent;

/**
 * Created by EvilEntity on 15/08/2015.
 */
public class Transform extends PooledComponent {
	public float x, y, rotation;

	public Transform set (float rotation) {
		this.rotation = rotation;
		return this;
	}

	public Transform set (float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public Transform set (float x, float y, float rotation) {
		this.x = x;
		this.y = y;
		this.rotation = rotation;
		return this;
	}
	@Override protected void reset () {
		set(0, 0, 0);
	}
}
