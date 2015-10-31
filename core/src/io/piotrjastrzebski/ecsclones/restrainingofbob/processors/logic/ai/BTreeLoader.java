package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai;

import com.artemis.*;
import com.artemis.annotations.Wire;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.leaf.Wait;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeParser;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.SerializationException;
import com.badlogic.gdx.utils.StreamUtils;
import io.piotrjastrzebski.ecsclones.base.util.Input;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBTree;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.Injectable;

import java.io.Reader;

/**
 * Created by PiotrJ on 31/08/15.
 */
@Wire
public class BTreeLoader extends BaseEntitySystem implements Input, InputProcessor {
	private final static String TAG = BTreeLoader.class.getSimpleName();
	protected ComponentMapper<EnemyBrain> mEnemyBrain;
	protected ComponentMapper<EnemyBTree> mEnemyBTree;
	public static final String DUMMY_BTREE = "dummy";

	public BTreeLoader () {
		super(Aspect.all(EnemyBrain.class));
	}

	ObjectMap<String, Pool<BehaviorTree<EnemyBrain>>> bTreePools = new ObjectMap<>();

	@Override protected void initialize () {
		archetypes.put(DUMMY_BTREE, new BehaviorTree<>(new Wait<EnemyBrain>()));
	}

	@Override protected void inserted (int eid) {
		EnemyBrain brain = mEnemyBrain.get(eid);
		EnemyBTree tree = mEnemyBTree.create(eid);
		tree.set(brain.treePath, get(brain.treePath));
	}

	ObjectMap<String, BehaviorTree<EnemyBrain>> archetypes = new ObjectMap<>();

	private BehaviorTree<EnemyBrain> get (final String path) {
		return get(path, false);
	}

	private BehaviorTree<EnemyBrain> get (final String path, final boolean reload) {
		Pool<BehaviorTree<EnemyBrain>> pool = bTreePools.get(path, null);
		if (pool == null) {
			pool = new Pool<BehaviorTree<EnemyBrain>>() {
				@Override protected BehaviorTree<EnemyBrain> newObject () {
					return (BehaviorTree<EnemyBrain>)getArchetype(path, reload).cloneTask();
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

	private BehaviorTree<EnemyBrain> getArchetype (String path, boolean reload) {
		BehaviorTree<EnemyBrain> tree = archetypes.get(path, null);
		if (tree == null || reload) {
			tree = load(path);
			if (tree != null) {
				archetypes.put(path, tree);
			} else {
				return archetypes.get(DUMMY_BTREE);
			}
		}
		return tree;
	}

	public BehaviorTree<EnemyBrain> load (String path) {
		return load(Gdx.files.internal(path));
	}

	public BehaviorTree<EnemyBrain> load (FileHandle file) {
		Reader reader = null;
		BehaviorTree<EnemyBrain> tree = null;
		try {
			reader = file.reader();
			BehaviorTreeParser<EnemyBrain> parser = new BehaviorTreeParser<>(BehaviorTreeParser.DEBUG_NONE);
			tree = parser.parse(reader, null);
			// cline tree tp force load included sub trees
			tree = (BehaviorTree<EnemyBrain>)tree.cloneTask();
			injectTask(tree);
		} catch (SerializationException e) {
			Gdx.app.error(TAG, "Reload of " + file + " failed!");
			e.printStackTrace();
		} finally {
			StreamUtils.closeQuietly(reader);
		}
		return tree;
	}

	public void injectTask (Task task) {
		if (task instanceof Injectable) {
			try {
				((Injectable)task).initialize(world);
			} catch (MundaneWireException e) {
				// we do not care if there is nothing to inject, perhaps we will at some point
			}
		} else if (task instanceof LeafTask) {
			Gdx.app.error(TAG, "All LeafTasks should extend BaseTask! " + task);
		}
		for (int i = 0; i < task.getChildCount(); i++) {
			injectTask( task.getChild(i));
		}
	}

	@Override protected void processSystem () {}

	@Override public int priority () {
		return 0;
	}

	@Override public InputProcessor get () {
		return this;
	}

	@Override public boolean keyDown (int keycode) {
		if (keycode == com.badlogic.gdx.Input.Keys.R) {
			Gdx.app.log(TAG, "Reloading behaviour trees...");
			IntBag entities = getSubscription().getEntities();
			for (int i = 0; i < entities.size(); i++) {
				int eid = entities.get(i);
				EnemyBrain brain = mEnemyBrain.get(eid);
				EnemyBTree tree = mEnemyBTree.get(eid);
				tree.set(brain.treePath, get(brain.treePath, true));
			}
		}
		return false;
	}

	@Override public boolean keyUp (int keycode) {
		return false;
	}

	@Override public boolean keyTyped (char character) {
		return false;
	}

	@Override public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override public boolean touchDragged (int screenX, int screenY, int pointer) {
		return false;
	}

	@Override public boolean mouseMoved (int screenX, int screenY) {
		return false;
	}

	@Override public boolean scrolled (int amount) {
		return false;
	}
}
