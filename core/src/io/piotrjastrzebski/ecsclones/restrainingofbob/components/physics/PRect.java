package io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 * Created by PiotrJ on 29/08/15.
 */
public class PRect extends PooledComponent {
	public float width;
	public float height;

	@Override protected void reset () {
		width = 0;
		height = 0;
	}

	public PRect setSize (float size) {
		this.width = size/2;
		this.height = size/2;
		return this;
	}

	public PRect setSize (float width, float height) {
		this.width = width/2;
		this.height = height/2;
		return this;
	}
}
