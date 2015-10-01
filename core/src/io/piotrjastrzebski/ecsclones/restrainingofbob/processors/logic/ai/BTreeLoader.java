package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai;

import com.artemis.*;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeParser;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.StreamUtils;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBTree;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.Injectable;

import java.io.Reader;

/**
 * Created by PiotrJ on 31/08/15.
 */
@Wire
public class BTreeLoader extends EntitySystem {
	private final static String TAG = BTreeLoader.class.getSimpleName();
	protected ComponentMapper<EnemyBrain> mEnemyBrain;
	protected ComponentMapper<EnemyBTree> mEnemyBTree;

	public BTreeLoader () {
		super(Aspect.all(EnemyBrain.class));
		setPassive(true);
	}

	ObjectMap<String, Pool<BehaviorTree<EnemyBrain>>> bTreePools = new ObjectMap<>();

	@Override protected void initialize () {
	}

	@Override protected void inserted (int eid) {
		EnemyBrain brain = mEnemyBrain.get(eid);
		EnemyBTree tree = world.getEntity(eid).edit().create(EnemyBTree.class);
		tree.set(brain.treePath, get(brain.treePath));
	}

	ObjectMap<String, BehaviorTree<EnemyBrain>> archetypes = new ObjectMap<>();

	private BehaviorTree<EnemyBrain> get (final String path) {
		Pool<BehaviorTree<EnemyBrain>> pool = bTreePools.get(path, null);
		if (pool == null) {
			pool = new Pool<BehaviorTree<EnemyBrain>>() {
				@Override protected BehaviorTree<EnemyBrain> newObject () {
					return (BehaviorTree<EnemyBrain>)getArchetype(path).cloneTask();
				}
			};
			bTreePools.put(path, pool);
		}
		return pool.obtain();
	}

	@Override protected void removed (int entityId) {
		EnemyBTree tree = mEnemyBTree.get(entityId);
		BehaviorTree<EnemyBrain> bt = tree.tree;
		bt.reset();
		bTreePools.get(tree.path).free(bt);
	}

	private BehaviorTree<EnemyBrain> getArchetype (String path) {
		BehaviorTree<EnemyBrain> tree = archetypes.get(path, null);
		if (tree == null) {
			tree = load(path);
			archetypes.put(path, tree);
		}
		return tree;
	}

	private BehaviorTree<EnemyBrain> load (String path) {
		Reader reader = null;
		BehaviorTree<EnemyBrain> tree = null;
		try {
			reader = Gdx.files.internal(path).reader();
			BehaviorTreeParser<EnemyBrain> parser = new BehaviorTreeParser<>(BehaviorTreeParser.DEBUG_NONE);
			tree = parser.parse(reader, null);
			// cline tree tp force load included sub trees
			tree = (BehaviorTree<EnemyBrain>)tree.cloneTask();
			injectTask(tree);
		} finally {
			StreamUtils.closeQuietly(reader);
		}
		return tree;
	}

	private void injectTask (Task task) {
		for (int i = 0; i < task.getChildCount(); i++) {
			Task child = task.getChild(i);
			if (child instanceof Injectable) {
				try {
					((Injectable)child).initialize(world);
				} catch (MundaneWireException e) {
					// we do not care if there is nothing to inject, perhaps we will at some point
				}
			} else if (child instanceof LeafTask) {
				Gdx.app.error(TAG, "All LeafTasks should extend BaseTask! " + child);
			}
			injectTask(child);
		}
	}

	@Override protected void processSystem () {}
}
