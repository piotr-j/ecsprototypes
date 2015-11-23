package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai;

import com.artemis.*;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.systems.IteratingSystem;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.BTWatcher;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.status.Dead;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBTree;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;

/**
 *
 * Created by PiotrJ on 31/08/15.
 */
@Wire
public class BTreeUpdater extends IteratingSystem {
	protected ComponentMapper<EnemyBrain> mEnemyBrain;
	protected ComponentMapper<EnemyBTree> mEnemyBTree;

	public BTreeUpdater () {
		super(Aspect.all(EnemyBrain.class, EnemyBTree.class).exclude(Dead.class, BTWatcher.class));
	}

	@Override protected void initialize () {}

	@Override protected void process (int eid) {
		// TODO make this frame rate independent like physics? if we target 30fps we should be fine
		// TODO could try delayed system in artemis 1.0 for this
		EnemyBrain brain = mEnemyBrain.get(eid);
		EnemyBTree tree = mEnemyBTree.get(eid);
		if (brain.treePath == null) {
			brain.fsm.update();
		} else {
			tree.tree.setObject(brain);
			tree.tree.step();
		}
	}
}
