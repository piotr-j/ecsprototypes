package io.piotrjastrzebski.ecsclones.base.components;

import com.artemis.PooledComponent;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by PiotrJ on 04/08/15.
 */
public class Asset extends PooledComponent {
	public String name;
	public transient TextureRegion region;

	@Override protected void reset () {
		name = null;
		region = null;
	}
}
