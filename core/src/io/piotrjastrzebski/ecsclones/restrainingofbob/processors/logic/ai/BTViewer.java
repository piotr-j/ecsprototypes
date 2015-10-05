
package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai;

import com.artemis.*;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import com.badlogic.gdx.ai.btree.annotation.TaskConstraint;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Selection;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.Annotation;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.kotcrab.vis.ui.widget.*;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.BTWatcher;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.SBehaviour;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.status.Dead;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBTree;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;

/**
 *
 * Created by PiotrJ on 31/08/15.
 */
@Wire
public class BTViewer extends IteratingSystem {
	protected ComponentMapper<EnemyBrain> mEnemyBrain;
	protected ComponentMapper<EnemyBTree> mEnemyBTree;
	protected ComponentMapper<BTWatcher> mBTWatcher;
	protected ComponentMapper<SBehaviour> mSBehaviour;

	protected Steering steering;

	@Wire
	Stage stage;

	VisWindow window;
	VisTree tree;
	VisTable settings;
	boolean step = true;

	public BTViewer () {
		super(Aspect.all(EnemyBrain.class, EnemyBTree.class, BTWatcher.class).exclude(Dead.class));
	}

	BTListener listener;
	@Override protected void initialize () {
		listener = new BTListener();
		window = new VisWindow("BTV");
		stage.addActor(window);
		window.setResizable(true);

		settings = new VisTable(true);

		final VisTextButton toggleStep = new VisTextButton("STEP", "toggle");
		toggleStep.setChecked(step);
		toggleStep.addListener(new ClickListener(){
			@Override public void clicked (InputEvent event, float x, float y) {
				step = toggleStep.isChecked();
			}
		});
		window.add(toggleStep);
		window.row();
		VisTable labels = new VisTable(true);
		labels.add(new VisLabel("[GREEN]SUCCESS [ORANGE]RUNNING [RED]FAILURE\n[PURPLE] CANCELLED [GRAY]NOT RUN[]"));
		window.add(labels);
		window.row();

		tree = new VisTree();
		window.add(tree);
		window.add(settings);
		tree.addListener(new ChangeListener() {
			@Override public void changed (ChangeEvent event, Actor actor) {
				Selection<Tree.Node> selection = tree.getSelection();
				Tree.Node node = selection.getLastSelected();
				if (node == null) return;
				if (!(node instanceof ViewNode)) return;
				final ViewNode selected = (ViewNode)node;
				settings.clear();
				settings.add(new VisLabel(selected.label.getText()));
				Class<?> clazz = selected.task.getClass();
				Field[] fields = ClassReflection.getFields(clazz);
				for (Field f : fields) {
					Annotation a = f.getDeclaredAnnotation(TaskAttribute.class);
					if (a != null) {
						TaskAttribute annotation = a.getAnnotation(TaskAttribute.class);
						settings.row();
						VisLabel label = new VisLabel();
						try {
							boolean req = annotation.required();
							label.setText(
								((annotation.name() != null && annotation.name().length() > 0) ? annotation.name() : f.getName()) + (req ?
									"(R):" :
									":"));
							settings.add(label);
							final VisTextField textField = new VisTextField(String.valueOf(f.get(selected.task)));
							settings.add(textField);
							final Field field = f;
							Gdx.app.log("", "" + field.getType());
							textField.addListener(new ChangeListener() {
								@Override public void changed (ChangeEvent event, Actor actor) {
									Gdx.app.log("", "Change! " + textField.getText());
									if (field.getType() == float.class) {
										try {
											field.set(selected.task, Float.valueOf(textField.getText()));
										} catch (ReflectionException e) {
											e.printStackTrace();
										}
										Gdx.app.log("", "got a float!");
									} else if (field.getType() == String.class) {
										Gdx.app.log("", "got a String!");
										try {
											field.set(selected.task, textField.getText());
										} catch (ReflectionException e) {
											e.printStackTrace();
										}
									}
								}
							});

						} catch (ReflectionException e) {
							e.printStackTrace();
						}
					}
				}
				window.pack();
			}
		});
	}

	ObjectMap<Task<EnemyBrain>, ViewNode> taskToNode = new ObjectMap<>();
	@Override protected void inserted (int entityId) {
		// TODO make sure we only have one watched entity
		EnemyBTree tree = mEnemyBTree.get(entityId);
		tree.tree.reset();
		updateView(tree.tree.getChild(0));
		tree.tree.addListener(listener.clear(entityId));
		listener.parent(entityId);
	}

	private void updateView (Task<EnemyBrain> task) {
		tree.clearChildren();
		taskToNode.clear();
		updateView(task, null);
		tree.expandAll();
		window.pack();
	}

	private void updateView (Task<EnemyBrain> task, ViewNode parent) {
		ViewNode node = new ViewNode(task);
		taskToNode.put(task, node);
		if (parent == null) {
			tree.add(node);
		} else {
			parent.add(node);
		}
		for (int i = 0; i < task.getChildCount(); i++) {
			updateView(task.getChild(i), node);
		}
	}

	@Override protected void process (int eid) {
		EnemyBrain brain = mEnemyBrain.get(eid);
		EnemyBTree tree = mEnemyBTree.get(eid);
		// entity is excluded from normal updates if it is watched, so we cam manipulate the tree
		if (step) {
			// NOTE do we want to handle steering as well?
			if (mSBehaviour.has(eid))
				steering.process(eid);
			tree.tree.setObject(brain);
			tree.tree.step();
			// NOTE we cant rebuild the tree each time, need to figure out reload from file maybe?
//			updateView(tree.tree);
//			tree.tree.addListener(listener.clear(eid));
		}
	}

	@Override protected void end () {
		stage.act();
		stage.draw();
	}

	@Override protected void removed (int entityId) {
		listener.clear();
		clearView();
	}

	private void clearView () {
		tree.clearChildren();
	}

	private class ParamNode extends VisTree.Node {
		public ParamNode (final Task<EnemyBrain> task, final Field field, TaskAttribute annotation) {
			super(new VisTable(true));
			VisTable t = (VisTable)getActor();
			VisLabel label = new VisLabel();
			t.add(label);

			boolean req = annotation.required();
			String name = annotation.name();
			String reqStr = (req?"(R):":":");
			if (name != null && name.length() > 0) {
				label.setText(name + reqStr);
			} else {
				label.setText(field.getName() + reqStr);
			}

			// TODO drop down for enums/booleans
			final VisTextField tf = new VisTextField();
			t.add(tf);
			try {
				tf.setText(String.valueOf(field.get(task)));
			} catch (ReflectionException e) {
				e.printStackTrace();
			}

			// TODO validators
			tf.addListener(new ChangeListener() {
				@Override public void changed (ChangeEvent event, Actor actor) {
					setField(task, field, tf.getText());
				}
			});
		}

		private void setField(Task<EnemyBrain> task, Field field, String value) {
			try {
				// do we need this?
//				field.setAccessible(true);
				if (field.getType() == float.class) {
					try {
						field.set(task, Float.valueOf(value));
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				} else if (field.getType() == String.class) {
					field.set(task, value);
				}
			} catch (ReflectionException e) {
				e.printStackTrace();
			}
		}
	}

	private class ViewNode extends VisTree.Node {
		Task<EnemyBrain> task;
		VisLabel label;
		VisTable cont;
		public ViewNode (final Task<EnemyBrain> task) {
			super(new VisLabel());
			this.task = task;
			label = (VisLabel)getActor();
			update(task);
			Class<?> clazz = task.getClass();
			Field[] fields = ClassReflection.getFields(clazz);
			for (Field f : fields) {
				Annotation a = f.getDeclaredAnnotation(TaskAttribute.class);
				if (a != null) {
					TaskAttribute annotation = a.getAnnotation(TaskAttribute.class);
					settings.row();
					add(new ParamNode(task, f, annotation));
				}
			}
		}

		public void update(Task<EnemyBrain> task) {
			this.task = task;
			Task.Status status = task.getStatus();
			String statusColor = " [GRAY]";
			if (status != null) {
				switch (status) {
				case SUCCEEDED:
					statusColor = " [GREEN]";
					break;
				case RUNNING:
					statusColor = " [ORANGE]";
					break;
				case FAILED:
					statusColor = " [RED]";
					break;
				case CANCELLED:
					statusColor = " [PURPLE]";
					break;
				}
			}
			String text = statusColor + task.getClass().getSimpleName() + "[]";
			if (task instanceof LeafTask) {
				text += " L";
			}
			label.setText(text);
		}

		@Override public String toString () {
			return "ViewNode{" +
				task.getClass().getSimpleName() +
				'}';
		}
	}

	private class BTListener implements BehaviorTree.Listener<EnemyBrain> {
		public int parentId = -1;
		@Override public void statusUpdated (Task<EnemyBrain> task, Task.Status previousStatus) {
			taskToNode.get(task).update(task);
		}

		public BTListener clear () {
			return clear(-1);
		}
		public BTListener clear (int entityId) {
			if (parentId >= 0 && entityId == parentId) {
				EnemyBTree tree = mEnemyBTree.getSafe(entityId);
				if (tree != null) tree.tree.removeListener(this);
			}
			parentId = entityId;
			return this;
		}

		public void parent (int entityId) {
			parentId = entityId;
		}
	}
}
