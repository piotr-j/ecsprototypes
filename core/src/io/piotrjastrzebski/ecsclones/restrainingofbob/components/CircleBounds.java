package io.piotrjastrzebski.ecsclones.restrainingofbob.components;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Circle;

/**
 * Created by PiotrJ on 26/08/15.
 */
public class CircleBounds extends PooledComponent {
	public Circle bounds = new Circle();
	public float radius;
	@Override protected void reset () {
		radius = 0;
		bounds.set(0, 0, radius);
	 }

	public CircleBounds radius (float radius) {
		this.radius = radius;
		bounds.radius = radius;
		return this;
	}

	public CircleBounds setSize (float size) {
		return radius(size / 2);
	}
}
