package io.piotrjastrzebski.ecsclones.restrainingofbob.utils.btedit;

import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.kotcrab.vis.ui.widget.*;

/**
 * Live visualization and editing for behaviour trees
 *
 * Created by PiotrJ on 06/10/15.
 */
class BTEditWindow extends VisWindow {
	VisTable topTable;
	VisTable treeTable;
	VisTable editTable;
	VisTable addTable;

	VisTree treeView;
	VisLabel trash;

	BTETaskEdit edit;

	BehaviorTree bt;

	public BTEditWindow () {
		super("BTEdit");
		debugAll();
		setResizable(true);
		setSize(900, 1200);
		topTable = new VisTable(true);
		trash = new VisLabel("Trash -> [_]");
		topTable.add(trash);

		treeTable = new VisTable(true);
		editTable = new VisTable(true);
		addTable = new VisTable(true);

		edit = new BTETaskEdit();
		editTable.add(edit).expand().fill();
		edit.setVisible(false);

		add(topTable).expandX().fillX().colspan(3);
		row();
		add(treeTable).fill();
		add(editTable).expand().fill();
		add(addTable).fill();

		treeView = new VisTree();
		treeTable.add(treeView).expand().fill();
	}

	public void set (BehaviorTree bt) {
		if (bt != null) {
			addView(bt);
		} else {
			clearView();
		}
	}

	private void addView (BehaviorTree bt) {
		if (this.bt != null) {
			clearView();
		}
		this.bt = bt;
		addViews(bt, null);
		treeView.expandAll();
		treeView.addListener(new ChangeListener() {
			@Override public void changed (ChangeEvent event, Actor actor) {
				TaskNode node = (TaskNode)treeView.getSelection().getLastSelected();
				if (node == null) {
					edit.setVisible(false);
					return;
				}
				edit.init(node.task);
				edit.setVisible(true);
			}
		});
	}

	private void addViews(Task task, TaskNode parent) {
		TaskNode node = obtain();
		if (parent == null) {
			treeView.add(node.init(task));
		} else {
			parent.add(node.init(task));
		}
		for (int i = 0; i < task.getChildCount(); i++) {
			addViews(task.getChild(i), node);
		}
	}

	private void clearView () {
		bt = null;
		for (Tree.Node node : treeView.getNodes()) {
			free((TaskNode)node);
		}
		treeView.clearChildren();
	}

	public void update () {

	}

	public void updateTasks (Array<Class<? extends Task>> registered) {
		// add registered task types so we can add them to the tree
		for (Actor child : addTable.getChildren()) {
			Pools.free(child);
		}
		addTable.clear();

		// TODO some sort of grouping/sort by type
		for (Class<? extends Task> aClass : registered) {
			VisLabel label = Pools.obtain(VisLabel.class);
			label.setText(aClass.getSimpleName());
			addTable.add(label).row();
		}
	}

	private TaskNode obtain () {
		return Pools.obtain(TaskNode.class);
	}

	private void free(TaskNode node) {
		Pools.free(node);
	}

	protected static class TaskNode extends VisTree.Node implements Pool.Poolable {
		private VisLabel label;
		private Task task;

		public TaskNode () {
			super(new VisLabel());
			label = (VisLabel)getActor();
		}

		public TaskNode init(Task task) {
			this.task = task;
			label.setText(task.getClass().getSimpleName());
			return this;
		}

		@Override public void reset () {
			task = null;
			// free any children
			Array<Tree.Node> children = getChildren();
			for (Tree.Node node : children) {
				Pools.free(node);
			}
			children.clear();
		}
	}

}
