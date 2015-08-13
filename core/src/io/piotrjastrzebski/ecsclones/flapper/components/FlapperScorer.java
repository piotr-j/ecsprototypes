package io.piotrjastrzebski.ecsclones.flapper.components;

import com.artemis.PooledComponent;

/**
 * Created by PiotrJ on 08/08/15.
 */
public class FlapperScorer extends PooledComponent {
	public boolean consumed;

	@Override protected void reset () {
		consumed = false;
	}
}
