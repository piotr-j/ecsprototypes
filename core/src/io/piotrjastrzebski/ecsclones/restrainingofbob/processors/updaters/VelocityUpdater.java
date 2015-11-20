package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.updaters;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.Velocity;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.PBody;

/**
 * Created by PiotrJ on 29/08/15.
 */
@Wire
public class VelocityUpdater extends IteratingSystem {
	protected ComponentMapper<PBody> mPBody;
	protected ComponentMapper<Velocity> mVelocity;
	public VelocityUpdater () {
		super(Aspect.all(Velocity.class, PBody.class));
	}

	@Override protected void process (int eid) {
		Velocity velocity = mVelocity.get(eid);
		Body body = mPBody.get(eid).body;
		velocity.vel.set(body.getLinearVelocity());
	}
}
