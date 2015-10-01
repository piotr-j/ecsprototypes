package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.updaters;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.CircleBounds;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.Transform;

/**
 * Created by PiotrJ on 29/08/15.
 */
@Wire
public class CircleBoundsUpdater extends EntityProcessingSystem{
	protected ComponentMapper<Transform> mTransform;
	protected ComponentMapper<CircleBounds> mCircleBounds;

	public CircleBoundsUpdater () {
		super(Aspect.all(Transform.class, CircleBounds.class));
	}

	@Override protected void process (Entity e) {
		CircleBounds circleBounds = mCircleBounds.get(e);
		Transform transform = mTransform.get(e);
		circleBounds.bounds.setPosition(transform.pos.x + circleBounds.radius, transform.pos.y + circleBounds.radius);
	}
}
