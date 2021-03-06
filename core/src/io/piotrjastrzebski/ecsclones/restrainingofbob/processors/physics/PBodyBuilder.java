package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.physics;

import com.artemis.*;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.Transform;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.*;

/**
 * Created by PiotrJ on 22/08/15.
 */
@Wire
public class PBodyBuilder extends BaseEntitySystem {
	protected ComponentMapper<Transform> mTransform;
	protected ComponentMapper<PBodyDef> mPBodyDef;
	protected ComponentMapper<PBody> mPBody;
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

	@Override protected void inserted (int eid) {
		Transform transform = mTransform.get(eid);
		PBodyDef pBodyDef = mPBodyDef.get(eid);
		pBodyDef.rotation(transform.rot);
		pBodyDef.position(transform.pos);



		PBody pBody = mPBody.create(eid);
		Body body = physics.getB2DWorld().createBody(pBodyDef.def);

		if (fixtureDef == null) fixtureDef = new FixtureDef();
		fixtureDef.restitution = pBodyDef.restitution;
		fixtureDef.friction = pBodyDef.friction;
		fixtureDef.density = pBodyDef.density;
		fixtureDef.filter.categoryBits = pBodyDef.categoryBits;
		fixtureDef.filter.maskBits = pBodyDef.maskBits;
		fixtureDef.filter.groupIndex = pBodyDef.groupIndex;

		PRect pRect = mPRect.getSafe(eid);
		if (pRect != null) {
			if (polygon == null) polygon = new PolygonShape();
			polygon.setAsBox(pRect.width, pRect.height);
			fixtureDef.shape = polygon;
			body.createFixture(fixtureDef);

			body.setTransform(transform.pos.x + pRect.width, transform.pos.y + pRect.height, transform.rot);
		}

		PPolygon pPolygon = mPPolygon.getSafe(eid);
		if (pPolygon != null) {
			if (polygon == null) polygon = new PolygonShape();
			polygon.set(pPolygon.get());
			fixtureDef.shape = polygon;
			body.createFixture(fixtureDef);
			Rectangle bounds = pPolygon.getBounds();
			body.setTransform(transform.pos.x + bounds.width / 2, transform.pos.y +  + bounds.height / 2, transform.rot);
		}

		PCircle pCircle = mPCircle.getSafe(eid);
		if (pCircle != null) {
			if (circle == null) circle = new CircleShape();
			circle.setRadius(pCircle.radius);
			circle.setPosition(pCircle.pos);
			fixtureDef.shape = circle;
			body.createFixture(fixtureDef);
			body.setTransform(transform.pos.x + pCircle.radius, transform.pos.y + pCircle.radius, transform.rot);
		}

		pBody.body = body;
		Physics.UserData userData = pBodyDef.userData != null?pBodyDef.userData:new Physics.UserData(eid);
		userData.steerable = mPSteerable.getSafe(eid);
		body.setUserData(userData);
//		PEdge pEdge = mPEdge.getSafe(e);
//		if (pEdge != null) {
//			if (edge == null) edge = new EdgeShape();
//
//			fixtureDef.shape = edge;
//			body.createFixture(fixtureDef);
//		}
	}

	@Override protected void removed (int eid) {

	}

	@Override protected void processSystem () {
	}

	@Override protected void dispose () {
		super.dispose();
	}
}
