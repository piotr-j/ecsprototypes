package io.piotrjastrzebski.ecsclones.restrainingofbob.components;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by PiotrJ on 26/08/15.
 */
public class Mover extends PooledComponent {
	public float maxLinearImp;
	public Vector2 linearImp = new Vector2();
	public float maxAngularImp;
	public float angularImp;
	@Override protected void reset () {
		maxLinearImp = 0;
		linearImp.setZero();
		maxAngularImp = 0;
		angularImp = 0;
	}
}
