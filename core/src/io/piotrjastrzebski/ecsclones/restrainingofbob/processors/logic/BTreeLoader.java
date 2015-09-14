package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic;

import com.artemis.*;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeParser;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.StreamUtils;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.EnemyBTree;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.BaseDecorator;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.BaseTask;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.Injectable;

import java.io.Reader;

/**
 * Created by PiotrJ on 31/08/15.
 */
@Wire
public class BTreeLoader extends EntitySystem {
	private final static String TAG = BTreeLoader.class.getSimpleName();
	protected ComponentMapper<EnemyBrain> mEnemyBrain;

	public BTreeLoader () {
		super(Aspect.all(EnemyBrain.class));
		setPassive(true);
	}

	@Override protected void initialize () {}

	@Override protected void inserted (int eid) {
		EnemyBrain brain = mEnemyBrain.get(eid);
		EnemyBTree tree = world.getEntity(eid).edit().create(EnemyBTree.class);
		tree.tree = (BehaviorTree<EnemyBrain>)get(brain.treePath).cloneTask();
	}

	ObjectMap<String, BehaviorTree<EnemyBrain>> trees = new ObjectMap<>();
	private BehaviorTree<EnemyBrain> get (String path) {
		BehaviorTree<EnemyBrain> tree = trees.get(path);
		if (tree == null) {
			tree = load(path);
			trees.put(path, tree);
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
