package io.piotrjastrzebski.ecsclones.restrainingofbob.components.rendering;

import com.artemis.PooledComponent;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by PiotrJ on 26/08/15.
 */
public class Renderable extends PooledComponent {
	// cache these somewhere?
	public Sprite sprite;

	@Override protected void reset () {
		sprite = null;
	}
}
