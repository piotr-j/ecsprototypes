package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.physics;

import com.artemis.*;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.World;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.Transform;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.*;

import java.lang.ref.PhantomReference;

/**
 * Created by PiotrJ on 22/08/15.
 */
@Wire
public class PBodyBuilder extends EntitySystem {
	protected ComponentMapper<Transform> mTransform;
	protected ComponentMapper<PBodyDef> mPBodyDef;
	protected ComponentMapper<PRect> mPRect;
	protected ComponentMapper<PCircle> mPCircle;
	protected ComponentMapper<PPolygon> mPPolygon;
	protected ComponentMapper<PEdge> mPEdge;
	protected ComponentMapper<PSteerable> mPSteerable;

	@Wire Physics physics;

	public PBodyBuilder () {
		super(Aspect.all(Transform.class, PBodyDef.class)
			.one(PRect.class, PCircle.class, PPolygon.class, PEdge.class));
	}

	FixtureDef fixtureDef;
	PolygonShape polygon;
	CircleShape circle;
	EdgeShape edge;
	// chain ?

	@Override protected void inserted (Entity e) {
		Transform transform = mTransform.get(e);
		PBodyDef pBodyDef = mPBodyDef.get(e);
		pBodyDef.rotation(transform.rot);
		pBodyDef.position(transform.pos);



		PBody pBody = e.edit().create(PBody.class);
		Body body = physics.getWorld().createBody(pBodyDef.def);

		if (fixtureDef == null) fixtureDef = new FixtureDef();
		fixtureDef.restitution = pBodyDef.restitution;
		fixtureDef.friction = pBodyDef.friction;
		fixtureDef.density = pBodyDef.density;
		fixtureDef.filter.categoryBits = pBodyDef.categoryBits;
		fixtureDef.filter.maskBits = pBodyDef.maskBits;
		fixtureDef.filter.groupIndex = pBodyDef.groupIndex;

		PRect pRect = mPRect.getSafe(e);
		if (pRect != null) {
			if (polygon == null) polygon = new PolygonShape();
			polygon.setAsBox(pRect.width, pRect.height);
			fixtureDef.shape = polygon;
			body.createFixture(fixtureDef);

			body.setTransform(transform.pos.x + pRect.width, transform.pos.y + pRect.height, transform.rot);
		}

		PPolygon pPolygon = mPPolygon.getSafe(e);
		if (pPolygon != null) {
			if (polygon == null) polygon = new PolygonShape();
			polygon.set(pPolygon.get());
			fixtureDef.shape = polygon;
			body.createFixture(fixtureDef);
			Rectangle bounds = pPolygon.getBounds();
			body.setTransform(transform.pos.x + bounds.width / 2, transform.pos.y +  + bounds.height / 2, transform.rot);
		}

		PCircle pCircle = mPCircle.getSafe(e);
		if (pCircle != null) {
			if (circle == null) circle = new CircleShape();
			circle.setRadius(pCircle.radius);
			circle.setPosition(pCircle.pos);
			fixtureDef.shape = circle;
			body.createFixture(fixtureDef);
			body.setTransform(transform.pos.x + pCircle.radius, transform.pos.y + pCircle.radius, transform.rot);
		}

		pBody.body = body;
		Physics.UserData userData = new Physics.UserData(e);
		userData.steerable = mPSteerable.getSafe(e);
		body.setUserData(userData);
//		PEdge pEdge = mPEdge.getSafe(e);
//		if (pEdge != null) {
//			if (edge == null) edge = new EdgeShape();
//
//			fixtureDef.shape = edge;
//			body.createFixture(fixtureDef);
//		}
	}

	@Override protected void removed (Entity e) {

	}

	@Override protected void processSystem () {
	}

	@Override protected void dispose () {
		super.dispose();
	}
}
