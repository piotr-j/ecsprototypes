package io.piotrjastrzebski.ecsclones.restrainingofbob.components;

import com.artemis.PooledComponent;

/**
 * Created by PiotrJ on 26/08/15.
 */
public class HitBy extends PooledComponent {
	public int type;
	public float dmg;

	@Override protected void reset () {
		dmg = 0;
		type = 0;
	}
}
