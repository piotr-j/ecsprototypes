package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai;

import com.artemis.*;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.status.Dead;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBTree;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;

/**
 *
 * Created by PiotrJ on 31/08/15.
 */
@Wire
public class BTreeUpdater extends EntityProcessingSystem {
	protected ComponentMapper<EnemyBrain> mEnemyBrain;
	protected ComponentMapper<EnemyBTree> mEnemyBTree;

	public BTreeUpdater () {
		super(Aspect.all(EnemyBrain.class, EnemyBTree.class).exclude(Dead.class));
		setPassive(true);
	}

	@Override protected void initialize () {}

	@Override protected void process (Entity e) {
		// TODO make this frame rate independent like physics? if we target 30fps we should be fine
		// TODO could try delayed system in artemis 1.0 for this
		EnemyBrain brain = mEnemyBrain.get(e);
		EnemyBTree tree = mEnemyBTree.get(e);
		tree.tree.setObject(brain);
		tree.tree.step();
	}
}
