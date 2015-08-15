package io.piotrjastrzebski.ecsclones.slinger.components.parts;

import com.artemis.PooledComponent;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by EvilEntity on 15/08/2015.
 */
public class Target extends PooledComponent {
	// TODO do we put all bodies in 1 component regardless of type?
	public Body body;
	@Override protected void reset () {
		body = null;
	}
}
