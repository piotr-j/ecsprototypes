package io.piotrjastrzebski.ecsclones.slinger.components.parts;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;

/**
 * TODO this should be fancier maybe
 * Created by EvilEntity on 15/08/2015.
 */
public class SlingDef extends PooledComponent {
	public float friction;
	public float restitution;
	public float length;
	public float dampingRatio;
	public float frequencyHz;
	public Vector2 anchor = new Vector2();

	@Override protected void reset () {
		friction = 0;
		restitution = 0;
		length = 0;
		dampingRatio = 0;
		frequencyHz = 0;
		anchor.setZero();
	}
}
