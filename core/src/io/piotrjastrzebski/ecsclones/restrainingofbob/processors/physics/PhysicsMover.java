package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.MathUtils;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.Dead;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.Mover;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.PBody;

/**
 * Created by PiotrJ on 22/08/15.
 */
@Wire
public class PhysicsMover extends EntityProcessingSystem {
	private final static String TAG = PhysicsMover.class.getSimpleName();

	protected ComponentMapper<PBody> mPBody;
	protected ComponentMapper<Mover> mMover;

	public PhysicsMover () {
		super(Aspect.all(PBody.class, Mover.class).exclude(Dead.class));
	}

	@Override protected void process (Entity e) {
		PBody pBody = mPBody.get(e);
		Mover mover = mMover.get(e);
		if (!mover.linearImp.isZero()) {
			pBody.body.applyLinearImpulse(mover.linearImp, pBody.body.getWorldCenter(), true);
		}

		if (!MathUtils.isZero(mover.angularImp)) {
			pBody.body.applyAngularImpulse(mover.angularImp, true);
		}
	}
}
