package io.piotrjastrzebski.ecsclones.restrainingofbob.components;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by PiotrJ on 26/08/15.
 */
public class Velocity extends PooledComponent {
	public Vector2 vel = new Vector2();
	@Override protected void reset () {
		vel.setZero();
	}
}
