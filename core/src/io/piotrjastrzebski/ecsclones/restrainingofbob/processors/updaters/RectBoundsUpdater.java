package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.updaters;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.RectBounds;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.Transform;

/**
 * Created by PiotrJ on 29/08/15.
 */
@Wire
public class RectBoundsUpdater extends EntityProcessingSystem{
	protected ComponentMapper<Transform> mTransform;
	protected ComponentMapper<RectBounds> mRectBounds;

	public RectBoundsUpdater () {
		super(Aspect.all(Transform.class, RectBounds.class));
	}

	@Override protected void process (Entity e) {
		Transform transform = mTransform.get(e);
		RectBounds rectBounds = mRectBounds.get(e);
		rectBounds.bounds.setPosition(transform.pos.x, transform.pos.y);
	}
}
