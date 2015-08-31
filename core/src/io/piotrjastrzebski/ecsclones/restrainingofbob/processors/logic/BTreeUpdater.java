package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic;

import com.artemis.*;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.EnemyBTree;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.EnemyBrain;

/**
 *
 * Created by PiotrJ on 31/08/15.
 */
@Wire
public class BTreeUpdater extends EntityProcessingSystem {
	protected ComponentMapper<EnemyBrain> mEnemyBrain;
	protected ComponentMapper<EnemyBTree> mEnemyBTree;

	public BTreeUpdater () {
		super(Aspect.all(EnemyBrain.class, EnemyBTree.class));
		setPassive(true);
	}

	@Override protected void initialize () {}

	@Override protected void process (Entity e) {
		EnemyBrain brain = mEnemyBrain.get(e);
		EnemyBTree tree = mEnemyBTree.get(e);
		tree.tree.setObject(brain);
		tree.tree.step();
	}
}
