package io.piotrjastrzebski.ecsclones.restrainingofbob.utils.btedit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.kotcrab.vis.ui.widget.*;

/**
 * Live visualization and editing for behaviour trees
 *
 * Created by PiotrJ on 06/10/15.
 */
class BTEditWindow<E> extends VisWindow implements BehaviorTree.Listener<E> {
	public static final Color COLOR_SUCCEEDED = new Color(Color.GREEN);
	public static final Color COLOR_RUNNING = new Color(Color.ORANGE);
	public static final Color COLOR_FAILED = new Color(Color.RED);
	public static final Color COLOR_CANCELLED = new Color(Color.PURPLE);
	public static final Color COLOR_DEFAULT = new Color(Color.WHITE);
	public static final Color COLOR_NOT_RUN = new Color(Color.GRAY);

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

	private void addView (BehaviorTree<E> bt) {
		if (this.bt != null) {
			clearView();
		}
		this.bt = bt;
		bt.addListener(this);
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
		taskToNode.put(task, node);
		if (parent == null) {
			// TODO some better name for root node
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
		taskToNode.remove(node.task);
		Pools.free(node);
	}

	ObjectMap<Task, TaskNode> taskToNode = new ObjectMap<>();
	@Override public void statusUpdated (Task<E> task, Task.Status previousStatus) {
		taskToNode.get(task).updateStatus(previousStatus);
	}

	public void childAdded(Task<E> task, int id) {
		Gdx.app.log("", "Added " + task + " at " + id);
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
			clearStatus();
			return this;
		}

		@Override public void reset () {
			task = null;
			clearStatus();
			// free any children
			Array<Tree.Node> children = getChildren();
			for (Tree.Node node : children) {
				Pools.free(node);
			}
			children.clear();
		}

		private void clearStatus () {
			label.setColor(COLOR_NOT_RUN);
		}

		public void updateStatus (Task.Status previousStatus) {
			// TODO what do we do with previous status?
			// TODO figure out a way to show actually running task
			if (task == null) {
				label.setColor(COLOR_DEFAULT);
				return;
			}
			Task.Status status = task.getStatus();
			if (status == null) {
				label.setColor(COLOR_NOT_RUN);
				return;
			}
//			Gdx.app.log("", "Status for " + task + " changed to " + status + " from " +previousStatus);
			switch (status) {
			case SUCCEEDED:
				label.setColor(COLOR_SUCCEEDED);
				break;
			case RUNNING:
				label.setColor(COLOR_RUNNING);
				break;
			case FAILED:
				label.setColor(COLOR_FAILED);
				break;
			case CANCELLED:
				label.setColor(COLOR_CANCELLED);
				break;
			default:
				label.setColor(COLOR_NOT_RUN);
			}
			if (status != previousStatus) {
				Color color = label.getColor();
				color.r *= .5f;
				color.g *= .5f;
				color.b *= .5f;
			}
		}
	}

}
