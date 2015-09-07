package io.piotrjastrzebski.ecsclones.restrainingofbob.components;

import com.artemis.PooledComponent;

/**
 * Created by PiotrJ on 26/08/15.
 */
public class Shooter extends PooledComponent {
	public float delay;
	public float dmg;
	public float timer;
	public float vel;
	public float alive;
	public float srcVelMult;
	public float velSpread;
	public float aliveSpread;
	public float dmgSpread;

	@Override protected void reset () {
		delay = 0;
		dmg = 0;
		dmgSpread = 0;
		timer = 0;
		vel = 0;
		alive = 0;
		srcVelMult = 0;
		velSpread = 0;
		aliveSpread = 0;
	}
}
