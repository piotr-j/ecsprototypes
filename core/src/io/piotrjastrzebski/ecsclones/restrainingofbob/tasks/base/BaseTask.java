package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.base;

import com.artemis.MundaneWireException;
import com.artemis.World;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.Injectable;

/**
 * Base task for all tasks we will use
 * Created by PiotrJ on 19/08/15.
 */
@Wire
public class BaseTask extends LeafTask<EnemyBrain> implements Injectable {
	protected World world;
	@Override public void run () {

	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		BaseTask base = (BaseTask)task;
		base.world = world;
		return task;
	}

	@Override public void initialize (World world) throws MundaneWireException {
		this.world = world;
		world.inject(this);
	}
}
