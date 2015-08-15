package io.piotrjastrzebski.ecsclones.slinger.components.parts;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by EvilEntity on 15/08/2015.
 */
public class Sling extends PooledComponent {
	// TODO do we put all bodies in 1 component regardless of type?
	public Body body;
	public float length;
	public float dampingRatio;
	public float frequencyHz;
	public Vector2 anchor = new Vector2();
	@Override protected void reset () {
		body = null;
		length = 0;
		dampingRatio = 0;
		frequencyHz = 0;
		anchor.setZero();
	}
}
