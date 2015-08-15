package io.piotrjastrzebski.ecsclones.slinger.components;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

/**
 * Created by EvilEntity on 15/08/2015.
 */
public class PhysicsBodyDef extends PooledComponent {
	// TODO could use cleaner way... component per body type or something?
	public float radius;
	public float restitution;
	public float friction;
	public float density;
	public short categoryBits;
	public short maskBits;
	public short groupBits;
	public boolean fixedRotation;
	public boolean sleepingAllowed;
	public boolean isBullet;
	public String udTag;
	public BodyDef.BodyType type;
	public Vector2 linearVelocity;
	public float linearDamping;
	public float angularVelocity;
	public float angularDamping;

	@Override protected void reset () {

	}
}
