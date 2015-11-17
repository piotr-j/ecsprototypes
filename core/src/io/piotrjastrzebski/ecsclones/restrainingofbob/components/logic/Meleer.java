package io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic;

import com.artemis.PooledComponent;

/**
 * Created by PiotrJ on 26/08/15.
 */
public class Meleer extends PooledComponent {
	public float delay;
	public float dmg;
	public float range;

	@Override protected void reset () {
		delay = 0;
		dmg = 0;
		range = 0;
	}
}
