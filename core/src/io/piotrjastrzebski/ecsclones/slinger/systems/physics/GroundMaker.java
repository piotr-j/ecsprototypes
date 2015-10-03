package io.piotrjastrzebski.ecsclones.slinger.systems.physics;

import com.artemis.*;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import io.piotrjastrzebski.ecsclones.slinger.components.Size;
import io.piotrjastrzebski.ecsclones.slinger.components.Transform;
import io.piotrjastrzebski.ecsclones.slinger.components.parts.Block;
import io.piotrjastrzebski.ecsclones.slinger.components.parts.BlockDef;
import io.piotrjastrzebski.ecsclones.slinger.components.parts.Ground;
import io.piotrjastrzebski.ecsclones.slinger.components.parts.GroundDef;

/**
 * Created by EvilEntity on 15/08/2015.
 */
@Wire
public class GroundMaker extends BaseEntitySystem {
	protected ComponentMapper<Ground> mGround;
	private ComponentMapper<GroundDef> mGroundDef;
	private ComponentMapper<Transform> mTransform;
	private ComponentMapper<Size> mSize;

	@Wire Physics physics;

	public GroundMaker () {
		super(Aspect.all(GroundDef.class, Transform.class, Size.class));
	}

	BodyDef bodyDef;
	PolygonShape shape;
	FixtureDef fixtureDef;
	@Override protected void inserted (int eid) {
		GroundDef groundDef = mGroundDef.get(eid);
		Transform tf = mTransform.get(eid);
		Size size = mSize.get(eid);

		Ground ground = mGround.create(eid);
		if (bodyDef == null) bodyDef = new BodyDef();

		bodyDef.type = BodyDef.BodyType.StaticBody;
		bodyDef.position.set(tf.x + size.width / 2, tf.y + size.height / 2);
		bodyDef.angle = tf.rotation * MathUtils.degreesToRadians;

		ground.body = physics.getB2DWorld().createBody(bodyDef);

		if (shape == null) shape = new PolygonShape();
		shape.setAsBox(size.width / 2, size.height / 2);

		if (fixtureDef == null) fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.restitution = groundDef.restitution;
		fixtureDef.friction = groundDef.friction;

		ground.body.createFixture(fixtureDef);
	}

	@Override protected void processSystem () {
		// do nothing
	}

	@Override protected void dispose () {
		if (shape != null) shape.dispose();
	}
}
