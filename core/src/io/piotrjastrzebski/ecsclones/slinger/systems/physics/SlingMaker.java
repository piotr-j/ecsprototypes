package io.piotrjastrzebski.ecsclones.slinger.systems.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import io.piotrjastrzebski.ecsclones.slinger.components.Size;
import io.piotrjastrzebski.ecsclones.slinger.components.Transform;
import io.piotrjastrzebski.ecsclones.slinger.components.parts.Block;
import io.piotrjastrzebski.ecsclones.slinger.components.parts.BlockDef;
import io.piotrjastrzebski.ecsclones.slinger.components.parts.Sling;
import io.piotrjastrzebski.ecsclones.slinger.components.parts.SlingDef;

/**
 * Created by EvilEntity on 15/08/2015.
 */
@Wire
public class SlingMaker extends EntitySystem {
	private ComponentMapper<SlingDef> mSlingDef;
	private ComponentMapper<Transform> mTransform;
	private ComponentMapper<Size> mSize;

	@Wire Physics physics;

	public SlingMaker () {
		super(Aspect.all(SlingDef.class, Transform.class, Size.class));
		setPassive(true);
	}

	int latestID;
	BodyDef bodyDef;
	PolygonShape shape;
	FixtureDef fixtureDef;
	@Override protected void inserted (int eid) {
		latestID = eid;

		Transform tf = mTransform.get(eid);
		Size size = mSize.get(eid);
		SlingDef slingDef = mSlingDef.get(eid);

		Sling sling = world.getEntity(eid).edit().create(Sling.class);
		sling.length = slingDef.length;
		sling.dampingRatio = slingDef.dampingRatio;
		sling.frequencyHz = slingDef.frequencyHz;
		sling.anchor.set(slingDef.anchor);

		if (bodyDef == null) bodyDef = new BodyDef();

		bodyDef.type = BodyDef.BodyType.StaticBody;
		bodyDef.position.set(tf.x + size.width / 2, tf.y + size.height / 2);
		bodyDef.angle = tf.rotation * MathUtils.degreesToRadians;

		sling.body = physics.getWorld().createBody(bodyDef);

		if (shape == null) shape = new PolygonShape();
		shape.setAsBox(size.width / 2, size.height / 2);

		if (fixtureDef == null) fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.restitution = slingDef.restitution;
		fixtureDef.friction = slingDef.friction;

		sling.body.createFixture(fixtureDef);
	}

	@Override protected void processSystem () {
		// do nothing
	}

	@Override protected void dispose () {
		if (shape != null) shape.dispose();
	}

	public int latestID () {
		return latestID;
	}
}
