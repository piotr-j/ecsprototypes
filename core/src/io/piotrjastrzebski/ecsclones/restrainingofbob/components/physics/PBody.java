package io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics;

import com.artemis.PooledComponent;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by PiotrJ on 26/08/15.
 */
public class PBody extends PooledComponent {
	public Body body;

	@Override protected void reset () {
		body = null;
	}
}
