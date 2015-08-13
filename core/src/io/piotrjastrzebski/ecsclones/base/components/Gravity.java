package io.piotrjastrzebski.ecsclones.base.components;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by PiotrJ on 04/08/15.
 */
public class Gravity extends PooledComponent {
	public Vector2 acc = new Vector2();

	@Override protected void reset () {
		acc.setZero();
	}
}
