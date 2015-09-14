package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.DeleteAfter;

/**
 * Created by PiotrJ on 31/08/15.
 */
@Wire
public class Deleter extends EntityProcessingSystem {
	protected ComponentMapper<DeleteAfter> mRemoveAfter;

	public Deleter () {
		super(Aspect.all(DeleteAfter.class));
	}

	@Override protected void process (Entity e) {
		DeleteAfter after = mRemoveAfter.get(e);
		after.timer += world.delta;
		if (after.timer > after.delay) {
			after.timer = 0;
			e.deleteFromWorld();
		}
	}
}
