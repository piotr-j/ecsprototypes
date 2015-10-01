package io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai;

import com.artemis.PooledComponent;
import com.badlogic.gdx.ai.btree.BehaviorTree;

/**
 * Created by PiotrJ on 31/08/15.
 */
public class EnemyBTree extends PooledComponent {
	public BehaviorTree<EnemyBrain> tree;
	public String path;

	@Override protected void reset () {
		tree = null;
		path = null;
	}

	public EnemyBTree set (String path, BehaviorTree<EnemyBrain> tree) {
		this.path = path;
		this.tree = tree;
		return this;
	}
}
