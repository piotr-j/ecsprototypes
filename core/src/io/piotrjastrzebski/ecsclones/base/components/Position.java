package io.piotrjastrzebski.ecsclones.base.components;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by PiotrJ on 04/08/15.
 */
public class Position extends PooledComponent {
	public Vector2 pos = new Vector2();

	@Override protected void reset () {
		pos.setZero();
	}
}
