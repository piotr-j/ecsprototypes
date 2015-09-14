package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks;

import com.artemis.MundaneWireException;
import com.artemis.World;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.ai.btree.Decorator;
import com.badlogic.gdx.ai.btree.Task;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.EnemyBrain;

/**
 * Base decorator for all decorators we will use
 * Created by PiotrJ on 19/08/15.
 */
@Wire
public class BaseDecorator extends Decorator<EnemyBrain> implements Injectable {
	World world;
	@Override public void run () {

	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		super.copyTo(task);
		// TODO add any injected fields
		BaseDecorator decorator = (BaseDecorator)task;
		decorator.world = world;
		return task;
	}

	@Override public void initialize (World world) throws MundaneWireException {
		this.world = world;
		world.inject(this);
	}
}
