package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.updaters;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.CircleBounds;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.PBody;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.RectBounds;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.Transform;

/**
 * Created by PiotrJ on 29/08/15.
 */
@Wire
public class TransformUpdater extends IteratingSystem {

	protected ComponentMapper<Transform> mTransform;
	protected ComponentMapper<PBody> mPBody;
	protected ComponentMapper<CircleBounds> mCircleBounds;
	protected ComponentMapper<RectBounds> mRectBounds;
	public TransformUpdater () {
		super(Aspect.all(Transform.class, PBody.class));
	}


	@Override protected void process (int eid) {
		Transform trans = mTransform.get(eid);
		Body body = mPBody.get(eid).body;
		Vector2 pos = body.getPosition();
		if (mCircleBounds.has(eid)) {
			float radius = mCircleBounds.get(eid).radius;
			trans.pos.set(pos.x - radius, pos.y - radius);
		} else if (mRectBounds.has(eid)) {
			Rectangle bounds = mRectBounds.get(eid).bounds;
			trans.pos.set(pos.x - bounds.width / 2, pos.y - bounds.height /2);
		} else {
			trans.pos.set(pos.x, pos.y);
		}
		Vector2 vel = body.getLinearVelocity();
		if (!vel.isZero(0.05f)) {
			trans.rot = vel.angle();
		}
	}

}
