package io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by PiotrJ on 29/08/15.
 */
public class PPolygon extends PooledComponent {
	public Polygon polygon = new Polygon();

	@Override protected void reset () {

	}

	public float[] get() {
		return polygon.getVertices();
	}

	public PPolygon set(float[] verts) {
		polygon.setVertices(verts);
		return this;
	}

	public Rectangle getBounds() {
		return polygon.getBoundingRectangle();
	}
}
