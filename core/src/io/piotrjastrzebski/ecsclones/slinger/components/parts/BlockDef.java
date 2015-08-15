package io.piotrjastrzebski.ecsclones.slinger.components.parts;

import com.artemis.PooledComponent;

/**
 * Simple rectangular block made from some material
 *
 * Created by EvilEntity on 15/08/2015.
 */
public class BlockDef extends PooledComponent {
	public float restitution;
	public float friction;
	public float density;
	// TODO some definition of hardness?

	@Override protected void reset () {
		restitution = 0;
		friction = 0;
		density = 0;
	}
}
