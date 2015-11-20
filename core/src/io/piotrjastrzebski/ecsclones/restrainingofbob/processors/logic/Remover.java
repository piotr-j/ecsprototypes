package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic;

import com.artemis.*;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.RemoveAfter;

/**
 * Created by PiotrJ on 31/08/15.
 */
@Wire
public class Remover extends IteratingSystem {
	protected ComponentMapper<RemoveAfter> mRemoveAfter;

	public Remover () {
		super(Aspect.all(RemoveAfter.class));
	}

	@Override protected void process (int eid) {
		RemoveAfter after = mRemoveAfter.get(eid);
		after.timer += world.delta;
		if (after.timer < after.delay) return;
		after.timer = 0;
		EntityEdit edit = world.edit(eid);
		for (Class<? extends Component> cls : after.clses)  {
			edit.remove(cls);
		}
		edit.remove(after);
	}
}
