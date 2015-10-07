package io.piotrjastrzebski.ecsclones.restrainingofbob.utils.btedit;

import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.branch.Parallel;
import com.badlogic.gdx.ai.btree.branch.Selector;
import com.badlogic.gdx.ai.btree.branch.Sequence;
import com.badlogic.gdx.ai.btree.decorator.*;
import com.badlogic.gdx.ai.btree.leaf.Wait;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

/**
 * Live visualization and editing for behaviour trees
 *
 * Created by PiotrJ on 06/10/15.
 */
public class BTEdit<T> {
	// we want to add category in here
	private static final Class[] defaults = new Class[] {
		Parallel.class, Sequence.class, Selector.class, AlwaysFail.class, AlwaysSucceed.class, Include.class, Invert.class,
		Repeat.class, SemaphoreGuard.class, UntilFail.class, UntilSuccess.class, Wait.class,
	};
	private BTEditWindow window;

	private BehaviorTree<T> bt;

	Array<Class<? extends Task>> registered = new Array<>();
	public BTEdit () {
		window = new BTEditWindow();
		registerAll(defaults);
	}

	/**
	 * Register a task class so it can be added to the behaviour tree dynamically
	 * Default tasks are added by default
	 * @param aClass to register
	 */
	public void register(Class<? extends Task> aClass) {
		// TODO check if task is not abstract etc
		if (!registered.contains(aClass, true)) {
			registered.add(aClass);
			window.updateTasks(registered);
		}
	}
	/**
	 * Register a list of task classes so they can be added to the behaviour tree dynamically
	 * Default tasks are added by default
	 * @param classes to register
	 */
	public void registerAll (Class<? extends Task>[] classes) {
		for (Class<? extends Task> aClass : classes) {
			register(aClass);
		}
	}

	/**
	 *	Set tree to watch/edit
	 * @param tree behaviour tree to work with
	 */
	public void set (BehaviorTree<T> tree) {
		if (bt != null) {
			reset();
		}
		bt = tree;
		window.set(bt);
	}

	/**
	 * @return current tree or null
	 */
	public BehaviorTree<T> get () {
		return bt;
	}

	/**
	 * update the underlying behaviour tree as needed
	 */
	public void step () {
		if (bt == null) return;
		// TODO allow for manual, default or none steps
		// per node would be nice, but probably not feasible
		bt.step();

		window.update();
	}

	/**
	 * Remove tree
	 */
	public void reset () {
		// TODO cleanup
		bt = null;
		window.set (null);
	}

	/**
	 * Adds editor window to the stage
	 * @param stage to add to
	 */
	public void addToStage (Stage stage) {
		stage.addActor(window);
		window.centerWindow();
	}
}
