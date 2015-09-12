package io.piotrjastrzebski.ecsclones.restrainingofbob.components;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by PiotrJ on 26/08/15.
 */
public class Transform extends PooledComponent {
	public Vector2 pos = new Vector2();
	public float rot;
	@Override protected void reset () {
		pos.setZero();
		rot = 0;
	}

	public Transform set (Transform o) {
		pos.set(o.pos);
		rot = o.rot;
		return this;
	}
}
