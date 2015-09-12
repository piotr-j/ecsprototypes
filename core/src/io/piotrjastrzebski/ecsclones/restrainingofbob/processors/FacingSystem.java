package io.piotrjastrzebski.ecsclones.restrainingofbob.processors;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.*;

/**
 * Created by PiotrJ on 29/08/15.
 */
@Wire
public class FacingSystem extends EntityProcessingSystem {
	protected ComponentMapper<Transform> mTransform;
	protected ComponentMapper<MoveFacing> mFacing;
	public FacingSystem () {
		super(Aspect.all(Transform.class, MoveFacing.class).exclude(Dead.class));
	}

	@Override protected void process (Entity e) {
		Transform transform = mTransform.get(e);
		MoveFacing facing = mFacing.get(e);
		facing.set(transform.rot);
	}
}
