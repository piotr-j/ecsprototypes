package io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.EdgeShape;

/**
 * Created by PiotrJ on 29/08/15.
 */
public class PEdge extends PooledComponent {
	public Vector2 pos = new Vector2();
	public Vector2 v1 = new Vector2();
	public Vector2 v2 = new Vector2();

	@Override protected void reset () {
		pos.setZero();
		v1.setZero();
		v2.setZero();
	}
}
