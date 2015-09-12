package io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic;

import com.artemis.PooledComponent;
import com.badlogic.gdx.ai.btree.BehaviorTree;

/**
 * Created by PiotrJ on 31/08/15.
 */
public class EnemyBTree extends PooledComponent {
	public BehaviorTree<EnemyBrain> tree;

	@Override protected void reset () {
		tree = null;
	}
}
