package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic;

import com.artemis.*;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.RemoveAfter;

/**
 * Created by PiotrJ on 31/08/15.
 */
@Wire
public class Remover extends EntityProcessingSystem {
	protected ComponentMapper<RemoveAfter> mRemoveAfter;

	public Remover () {
		super(Aspect.all(RemoveAfter.class));
	}

	@Override protected void process (Entity e) {
		RemoveAfter after = mRemoveAfter.get(e);
		after.timer += world.delta;
		if (after.timer > after.delay) {
			after.timer = 0;
			EntityEdit edit = e.edit();
			for (Class<? extends Component> cls : after.clses)  {
				edit.remove(cls);
			}
			edit.remove(after);
		}
	}
}
