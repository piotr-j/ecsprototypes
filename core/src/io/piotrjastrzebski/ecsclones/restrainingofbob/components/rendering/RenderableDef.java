package io.piotrjastrzebski.ecsclones.restrainingofbob.components.rendering;

import com.artemis.PooledComponent;

/**
 * Created by PiotrJ on 26/08/15.
 */
public class RenderableDef extends PooledComponent {
	// NOTE something else?
	public String name;

	@Override protected void reset () {
		name = null;
	}
}
