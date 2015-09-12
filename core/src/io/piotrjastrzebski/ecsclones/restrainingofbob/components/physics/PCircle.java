package io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by PiotrJ on 29/08/15.
 */
public class PCircle extends PooledComponent {
	public Vector2 pos = new Vector2();
	public float radius;

	@Override protected void reset () {
		pos.setZero();
		radius = 0;
	}

	public void setSize (float size) {
		this.radius = size/2;
	}
}
