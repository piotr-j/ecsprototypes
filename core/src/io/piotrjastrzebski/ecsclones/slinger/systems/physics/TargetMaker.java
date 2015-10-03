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
import io.piotrjastrzebski.ecsclones.slinger.components.parts.TargetDef;

/**
 * Created by EvilEntity on 15/08/2015.
 */
@Wire
public class TargetMaker extends BaseEntitySystem {
	protected ComponentMapper<Block> mBlock;
	private ComponentMapper<BlockDef> mBlockDef;
	private ComponentMapper<Transform> mTransform;
	private ComponentMapper<Size> mSize;

	@Wire Physics physics;

	public TargetMaker () {
		super(Aspect.all(TargetDef.class, Transform.class, Size.class));
	}

	BodyDef bodyDef;
	PolygonShape shape;
	FixtureDef fixtureDef;
	@Override protected void inserted (int eid) {
		BlockDef blockDef = mBlockDef.get(eid);
		Transform tf = mTransform.get(eid);
		Size size = mSize.get(eid);

		Block block = mBlock.create(eid);
		if (bodyDef == null) bodyDef = new BodyDef();

		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(tf.x + size.width / 2, tf.y + size.height / 2);
		bodyDef.angle = tf.rotation * MathUtils.degreesToRadians;

		block.body = physics.getB2DWorld().createBody(bodyDef);

		if (shape == null) shape = new PolygonShape();
		shape.setAsBox(size.width / 2, size.height / 2);

		if (fixtureDef == null) fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.restitution = blockDef.restitution;
		fixtureDef.friction = blockDef.friction;
		fixtureDef.density = blockDef.density;

		block.body.createFixture(fixtureDef);
	}

	@Override protected void processSystem () {
		// do nothing
	}

	@Override protected void dispose () {
		if (shape != null) shape.dispose();
	}
}
