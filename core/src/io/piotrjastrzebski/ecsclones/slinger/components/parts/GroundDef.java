package io.piotrjastrzebski.ecsclones.slinger.components.parts;

import com.artemis.PooledComponent;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * TODO this should be fancier maybe
 * Created by EvilEntity on 15/08/2015.
 */
public class GroundDef extends PooledComponent {
	public float friction;
	public float restitution;

	@Override protected void reset () {
		friction = 0;
		restitution = 0;
	}
}
