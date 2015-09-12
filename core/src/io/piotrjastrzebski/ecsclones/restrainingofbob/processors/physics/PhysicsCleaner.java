package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Wire;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.PBody;

/**
 * Created by PiotrJ on 22/08/15.
 */
@Wire
public class PhysicsCleaner extends EntitySystem {
	private final static String TAG = PhysicsCleaner.class.getSimpleName();

	protected ComponentMapper<PBody> mPBody;

	Physics physics;
	public PhysicsCleaner () {
		super(Aspect.all(PBody.class));
		setPassive(true);
	}

	@Override protected void processSystem () {}

	@Override protected void removed (int eid) {
		PBody pBody = mPBody.get(eid);
		physics.destroyBody(pBody.body);
	}
}
