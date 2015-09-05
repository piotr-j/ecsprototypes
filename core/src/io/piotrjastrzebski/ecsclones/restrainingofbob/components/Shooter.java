package io.piotrjastrzebski.ecsclones.restrainingofbob.components;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by PiotrJ on 26/08/15.
 */
public class Shooter extends PooledComponent {
	public float delay;
	public float dmg;
	public float timer;
	public float vel;

	@Override protected void reset () {
		delay = 0;
		dmg = 0;
		timer = 0;
		vel = 0;
	}
}
