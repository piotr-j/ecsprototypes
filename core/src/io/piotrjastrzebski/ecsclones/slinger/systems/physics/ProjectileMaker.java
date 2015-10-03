package io.piotrjastrzebski.ecsclones.slinger.systems.physics;

import com.artemis.*;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import io.piotrjastrzebski.ecsclones.slinger.components.Radius;
import io.piotrjastrzebski.ecsclones.slinger.components.Size;
import io.piotrjastrzebski.ecsclones.slinger.components.Transform;
import io.piotrjastrzebski.ecsclones.slinger.components.parts.Block;
import io.piotrjastrzebski.ecsclones.slinger.components.parts.BlockDef;
import io.piotrjastrzebski.ecsclones.slinger.components.parts.Projectile;
import io.piotrjastrzebski.ecsclones.slinger.components.parts.ProjectileDef;

/**
 * Created by EvilEntity on 15/08/2015.
 */
@Wire
public class ProjectileMaker extends BaseEntitySystem {
	protected ComponentMapper<Projectile> mProjectile;
	private ComponentMapper<ProjectileDef> mProjectileDef;
	private ComponentMapper<Transform> mTransform;
	private ComponentMapper<Radius> mRadius;

	@Wire Physics physics;

	public ProjectileMaker () {
		super(Aspect.all(ProjectileDef.class, Transform.class, Radius.class));
	}

	BodyDef bodyDef;
	CircleShape shape;
	FixtureDef fixtureDef;
	@Override protected void inserted (int eid) {
		ProjectileDef projectileDef = mProjectileDef.get(eid);
		Transform tf = mTransform.get(eid);
		Radius radius = mRadius.get(eid);

		Projectile projectile = mProjectile.create(eid);
		if (bodyDef == null) bodyDef = new BodyDef();

		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(tf.x + radius.radius, tf.y + radius.radius);
		bodyDef.angle = tf.rotation * MathUtils.degreesToRadians;

		projectile.body = physics.getB2DWorld().createBody(bodyDef);

		if (shape == null) shape = new CircleShape();
		shape.setRadius(radius.radius);

		if (fixtureDef == null) fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.restitution = projectileDef.restitution;
		fixtureDef.friction = projectileDef.friction;
		fixtureDef.density = projectileDef.density;

		projectile.body.createFixture(fixtureDef);
	}

	@Override protected void processSystem () {
		// do nothing
	}

	@Override protected void dispose () {
		if (shape != null) shape.dispose();
	}
}
