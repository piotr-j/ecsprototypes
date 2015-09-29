package io.piotrjastrzebski.ecsclones.restrainingofbob.components.rendering;

import com.artemis.PooledComponent;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by PiotrJ on 26/08/15.
 */
public class DebugTint extends PooledComponent {
	public Color color = new Color();
	public Color base = new Color();

	@Override protected void reset () {
		color.set(Color.WHITE);
	}

	public DebugTint setBase (Color base) {
		this.base.set(base);
		color.set(base);
		return this;
	}

	public DebugTint set (Color color) {
		this.color.set(color);
		return this;
	}

	public DebugTint setDdefault() {
		color.set(base);
		return this;
	}
}
