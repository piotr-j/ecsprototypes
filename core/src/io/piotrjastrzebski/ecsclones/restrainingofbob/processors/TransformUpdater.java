package io.piotrjastrzebski.ecsclones.restrainingofbob.processors;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import io.piotrjastrzebski.ecsclones.base.GameScreen;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.CamFollow;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.Transform;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.PBody;

/**
 * Created by PiotrJ on 29/08/15.
 */
@Wire
public class TransformUpdater extends EntityProcessingSystem {

	protected ComponentMapper<Transform> mTransform;
	protected ComponentMapper<PBody> mPBody;
	public TransformUpdater () {
		super(Aspect.all(Transform.class, PBody.class));
	}


	@Override protected void process (Entity e) {
		Transform trans = mTransform.get(e);
		Body body = mPBody.get(e).body;
		trans.pos.set(body.getPosition());
		trans.rot = body.getAngle() * MathUtils.radiansToDegrees;
	}

}
