package io.piotrjastrzebski.ecsclones.restrainingofbob.components;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by PiotrJ on 26/08/15.
 */
public class RectBounds extends PooledComponent {
	public Rectangle bounds = new Rectangle();
	public float width;
	public float height;
	@Override protected void reset () {
		bounds.set(0, 0, 0, 0);
		width = 0;
		height = 0;
	}

	public RectBounds setSize(float size) {
		return setSize(size, size);
	}

	public RectBounds setSize(float width, float height) {
		this.width = width;
		this.height = height;
		bounds.setSize(width, height);
		return this;
	}

	public RectBounds setWidth(float width) {
		this.width = width;
		bounds.setWidth(width);
		return this;
	}

	public RectBounds setHeight(float height) {
		this.height = height;
		bounds.setHeight(height);
		return this;
	}
}
