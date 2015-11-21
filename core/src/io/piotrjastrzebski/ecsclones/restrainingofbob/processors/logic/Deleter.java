package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.DeleteAfter;

/**
 * Created by PiotrJ on 31/08/15.
 */
@Wire
public class Deleter extends IteratingSystem {
	protected ComponentMapper<DeleteAfter> mRemoveAfter;

	public Deleter () {
		super(Aspect.all(DeleteAfter.class));
	}

	@Override protected void process (int eid) {
		DeleteAfter after = mRemoveAfter.get(eid);
		after.timer += world.delta;
		if (after.timer > after.delay) {
			after.timer = 0;
			world.delete(eid);
		}
	}
}
