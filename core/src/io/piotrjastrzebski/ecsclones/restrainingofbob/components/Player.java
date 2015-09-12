package io.piotrjastrzebski.ecsclones.restrainingofbob.components;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by PiotrJ on 26/08/15.
 */
public class Player extends PooledComponent {
	public String name;
	@Override protected void reset () {
		name = null;
	}
}
