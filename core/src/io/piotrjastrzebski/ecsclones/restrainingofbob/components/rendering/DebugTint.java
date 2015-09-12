package io.piotrjastrzebski.ecsclones.restrainingofbob.components.rendering;

import com.artemis.PooledComponent;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by PiotrJ on 26/08/15.
 */
public class DebugTint extends PooledComponent {
	public Color color = new Color();
	@Override protected void reset () {
		color.set(Color.WHITE);
	}
}
