package io.piotrjastrzebski.ecsclones.restrainingofbob.utils.btedit;

import com.artemis.utils.reflect.ClassReflection;
import com.artemis.utils.reflect.Field;
import com.artemis.utils.reflect.ReflectionException;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.BranchTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
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

	DragAndDrop dad;

	public BTEditWindow () {
		super("BTEdit");
		debugAll();
		setResizable(true);
		setSize(900, 1200);

		dad = new DragAndDrop();

		topTable = new VisTable(true);
		trash = new VisLabel("Trash -> [_]");
		topTable.add(trash);

		dad.addTarget(new DragAndDrop.Target(trash) {
			@Override public boolean drag (DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
				// TODO check source if it can be trashed
				return true;
			}

			@Override public void drop (DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
				// remove node and stuff
				TaskPayload tp = (TaskPayload)payload;
				removeTaskNode(tp.getTaskNode());
				Pools.free(tp);
			}
		});

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

	protected void removeTaskNode(TaskNode node) {
		Task task = node.task;
		// what do we do if we nuke the root?
		if (bt == task) {
			clearView();
			return;
		}
		remove(bt, task);
		// we need to reset as we modified internal structure
		bt.reset();
		// todo move this to free?
		node.remove();
		free(node);
	}

	private void remove(Task parent, Task toRemove) {
//		int count = parent.getChildCount();
		for (int i = 0; i < parent.getChildCount(); i++) {
			Task child = parent.getChild(i);
			if (child == toRemove) {
				// how do we handle decorators? remove all to the bottom or just decorator?
				if (!(parent instanceof BranchTask)) return;
				// looks like we have no simple way of removing a thing...
				// guess we need to rebuild the tree?
				try {
					Field field = ClassReflection.getDeclaredField(BranchTask.class, "children");
					field.setAccessible(true);
					Array<Task> children = (Array<Task>)field.get(parent);
					children.removeValue(toRemove, true);
					if (toRemove.getStatus() == Task.Status.RUNNING) {
						toRemove.cancel();
					}
					// if it was last child, remove the parent a well
//					if (children.size == 0) {
//						removeTaskNode(taskToNode.get(parent));
//					}
					return;
				} catch (ReflectionException e) {
					e.printStackTrace();
				}
			} else {
				remove(child, toRemove);
			}
		}
	}

	public void set (BehaviorTree<E> bt) {
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
			treeView.add(node.init(task, dad));
			node.fadeMin = 1;
			node.label.setColor(COLOR_RUNNING);
		} else {
			parent.add(node.init(task, dad));
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
		for (TaskNode node : taskToNode.values()) {
			node.update(Gdx.graphics.getDeltaTime());
		}
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
		TaskNode node = taskToNode.get(task, null);
		if (node != null) {
			node.updateStatus(previousStatus);
		} else {
			Gdx.app.log("", "Node not found for " + task);
		}
	}

	public void childAdded(Task<E> task, int id) {
		Gdx.app.log("", "Added " + task + " at " + id);
	}

	protected static class TaskNode extends VisTree.Node implements Pool.Poolable {
		private VisLabel label;
		private Task task;
		private DragAndDrop dad;
		private DragAndDrop.Source source;
		public TaskNode () {
			super(new VisLabel());
			// tree uses object for comparison when removing nodes
			setObject(this);
			label = (VisLabel)getActor();
		}

		public TaskNode init (Task task, DragAndDrop dad) {
			this.task = task;
			this.dad = dad;
			label.setText(task.getClass().getSimpleName());

			source = new DragAndDrop.Source(label) {
				@Override public DragAndDrop.Payload dragStart (InputEvent event, float x, float y, int pointer) {
					TaskPayload payload = Pools.obtain(TaskPayload.class);
					payload.init(label.getText().toString(), TaskNode.this);
					return payload;
				}
			};
			dad.addSource(source);
			clearStatus();
			return this;
		}

		float fade = 1;
		float fadeTime = 3f;
		float fadeMin = .5f;
		public void update (float delta) {
			Color color = label.getColor();
			if (fade > fadeMin) {
				fade -= 1.f / fadeTime * delta;
			}
			color.a = fade;
		}

		@Override public void reset () {
			task = null;
			fade = 1;
			dad.removeSource(source);
			source = null;
			dad = null;
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
			fade = 1;
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
		}
	}

	protected static class TaskPayload extends DragAndDrop.Payload implements Pool.Poolable {
		private VisLabel drag;
		private VisLabel valid;
		private VisLabel invalid;
		private TaskNode taskNode;

		public TaskPayload () {
			drag = new VisLabel();
			drag.setColor(Color.WHITE);
			setDragActor(drag);
			valid = new VisLabel();
			valid.setColor(Color.GREEN);
			setValidDragActor(valid);
			invalid = new VisLabel();
			invalid.setColor(Color.RED);
			setInvalidDragActor(invalid);
		}

		public void init (String text, TaskNode taskNode) {
			drag.setText(text);
			valid.setText(text);
			invalid.setText(text);
			this.taskNode = taskNode;
		}

		public TaskNode getTaskNode () {
			return taskNode;
		}

		@Override public void reset () {
			drag.setText("");
			valid.setText("");
			invalid.setText("");
			taskNode = null;
		}
	}

}
