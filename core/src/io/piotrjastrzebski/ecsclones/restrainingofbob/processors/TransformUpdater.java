package io.piotrjastrzebski.ecsclones.restrainingofbob.processors;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.*;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.PBody;

/**
 * Created by PiotrJ on 29/08/15.
 */
@Wire
public class TransformUpdater extends EntityProcessingSystem {

	protected ComponentMapper<Transform> mTransform;
	protected ComponentMapper<PBody> mPBody;
	protected ComponentMapper<CircleBounds> mCircleBounds;
	protected ComponentMapper<RectBounds> mRectBounds;
	public TransformUpdater () {
		super(Aspect.all(Transform.class, PBody.class));
	}


	@Override protected void process (Entity e) {
		Transform trans = mTransform.get(e);
		Body body = mPBody.get(e).body;
		Vector2 pos = body.getPosition();
		if (mCircleBounds.has(e)) {
			float radius = mCircleBounds.get(e).radius;
			trans.pos.set(pos.x - radius, pos.y - radius);
		} else if (mRectBounds.has(e)) {
			Rectangle bounds = mRectBounds.get(e).bounds;
			trans.pos.set(pos.x - bounds.width / 2, pos.y - bounds.height /2);
		} else {
			trans.pos.set(pos.x, pos.y);
		}
		trans.rot = body.getAngle() * MathUtils.radiansToDegrees;
	}

}
