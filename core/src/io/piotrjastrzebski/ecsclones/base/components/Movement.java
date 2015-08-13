package io.piotrjastrzebski.ecsclones.base.components;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by PiotrJ on 04/08/15.
 */
public class Movement extends PooledComponent {
	public Vector2 vel = new Vector2();
	public Vector2 acc = new Vector2();
	public float friction;

	@Override protected void reset () {
		vel.setZero();
		acc.setZero();
		friction = 0;
	}
}
