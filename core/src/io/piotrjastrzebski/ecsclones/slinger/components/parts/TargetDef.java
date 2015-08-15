package io.piotrjastrzebski.ecsclones.slinger.components.parts;

import com.artemis.PooledComponent;

/**
 * TODO this should be fancier maybe
 * Created by EvilEntity on 15/08/2015.
 */
public class TargetDef extends PooledComponent {
	public float friction;
	public float restitution;
	public float density;

	@Override protected void reset () {
		friction = 0;
		restitution = 0;
		density = 0;
	}
}
