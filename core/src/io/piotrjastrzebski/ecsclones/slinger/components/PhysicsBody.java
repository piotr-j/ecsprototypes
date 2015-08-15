package io.piotrjastrzebski.ecsclones.slinger.components;

import com.artemis.PooledComponent;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by EvilEntity on 15/08/2015.
 */
public class PhysicsBody extends PooledComponent {
	public Body body;

	@Override protected void reset () {
		body = null;
	}
}
