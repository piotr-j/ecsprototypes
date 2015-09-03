package io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

/**
 * Created by PiotrJ on 26/08/15.
 */
public class PBodyDef extends PooledComponent {
	// NOTE do we want this? we need to copy almost all if not all fields anyway
	public BodyDef def = new BodyDef();

	public float restitution;
	public float friction;
	public float density;
	public short categoryBits;
	public short groupIndex;
	public short maskBits;


	public PBodyDef () {reset();}


	public PBodyDef type(BodyDef.BodyType type) {
		def.type = type;
		return this;
	}


	public PBodyDef position(float x, float y) {
		def.position.set(x, y);
		return this;
	}

	public PBodyDef position(Vector2 position) {
		def.position.set(position);
		return this;
	}

	public PBodyDef rotation(float degrees) {
		def.angle = degrees * MathUtils.degreesToRadians;
		return this;
	}

	public PBodyDef angle(float radians) {
		def.angle = radians;
		return this;
	}

	@Override protected void reset () {
		def.type = BodyDef.BodyType.StaticBody;
		def.position.setZero();
		def.angle = 0;
		def.linearVelocity.setZero();
		def.angularVelocity = 0;
		def.linearDamping = 0;
		def.angularDamping = 0;
		def.bullet = false;
		def.fixedRotation = false;
		def.allowSleep = true;
		def.active = true;
		def.awake = true;
		def.gravityScale = 1;

		categoryBits = 0x0001;
		groupIndex = 0;
		maskBits = -1;

		restitution = 0;
		friction = 0;
		density = 1;
	}
}
