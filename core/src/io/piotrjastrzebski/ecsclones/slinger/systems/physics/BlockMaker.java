package io.piotrjastrzebski.ecsclones.slinger.systems.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import io.piotrjastrzebski.ecsclones.slinger.components.Size;
import io.piotrjastrzebski.ecsclones.slinger.components.Transform;
import io.piotrjastrzebski.ecsclones.slinger.components.parts.Block;
import io.piotrjastrzebski.ecsclones.slinger.components.parts.BlockDef;

/**
 * Created by EvilEntity on 15/08/2015.
 */
@Wire
public class BlockMaker extends EntitySystem {
	private ComponentMapper<Block> mBlock;
	private ComponentMapper<BlockDef> mBlockDef;
	private ComponentMapper<Transform> mTransform;
	private ComponentMapper<Size> mSize;

	@Wire Physics physics;

	public BlockMaker () {
		super(Aspect.all(BlockDef.class, Transform.class, Size.class));
		setPassive(true);
	}

	BodyDef bodyDef;
	PolygonShape shape;
	FixtureDef fixtureDef;
	@Override protected void inserted (final Entity e) {
		BlockDef blockDef = mBlockDef.get(e);
		Transform tf = mTransform.get(e);
		Size size = mSize.get(e);

		Block block = e.edit().create(Block.class);
		if (bodyDef == null) bodyDef = new BodyDef();

		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(tf.x + size.width / 2, tf.y + size.height / 2);
		bodyDef.angle = tf.rotation * MathUtils.degreesToRadians;

		block.body = physics.getWorld().createBody(bodyDef);

		if (shape == null) shape = new PolygonShape();
		shape.setAsBox(size.width / 2, size.height / 2);

		if (fixtureDef == null) fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.restitution = blockDef.restitution;
		fixtureDef.friction = blockDef.friction;
		fixtureDef.density = blockDef.density;

		block.body.createFixture(fixtureDef);

		block.body.setUserData(new Physics.UserData(e){
			@Override public void onPostSolve (Physics.UserData userData, float strength) {
				if (strength > 1) {
					e.deleteFromWorld();
				}
			}
		});
	}

	@Override protected void removed (Entity e) {
		Block block = mBlock.get(e);
		physics.getWorld().destroyBody(block.body);
	}

	@Override protected void processSystem () {
		// do nothing
	}

	@Override protected void dispose () {
		if (shape != null) shape.dispose();
	}
}
