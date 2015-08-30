package io.piotrjastrzebski.ecsclones.restrainingofbob.components;

import com.artemis.PooledComponent;

/**
 * Created by PiotrJ on 26/08/15.
 */
public class CamFollow extends PooledComponent {
	public float offsetX;
	public float offsetY;
	@Override protected void reset () {
		offsetX = 0;
		offsetY = 0;
	}
}
