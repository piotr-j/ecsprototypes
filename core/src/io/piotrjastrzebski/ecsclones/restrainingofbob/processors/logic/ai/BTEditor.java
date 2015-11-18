
package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.branch.*;
import com.badlogic.gdx.ai.btree.decorator.*;
import com.badlogic.gdx.ai.btree.leaf.Failure;
import com.badlogic.gdx.ai.btree.leaf.Success;
import com.badlogic.gdx.ai.btree.leaf.Wait;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import io.piotrjastrzebski.bteditor.core.BehaviorTreeEditor;
import io.piotrjastrzebski.bteditor.core.IPersist;
import io.piotrjastrzebski.bteditor.core.model.TaskLibrary;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.BTWatcher;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBTree;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.SBehaviour;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.status.Dead;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.actions.*;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.conditions.HPAboveTask;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.conditions.InAttackRangeTask;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.conditions.InRangeTask;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.conditions.IsAliveTask;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.stacks.IsValueSet;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.stacks.StackIsEmptyTask;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.stacks.StackPopTask;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.stacks.StackPushTask;

/**
 *
 * Created by PiotrJ on 31/08/15.
 */
@Wire
public class BTEditor extends IteratingSystem {
	private static final String TAG = BTEditor.class.getSimpleName();

	protected ComponentMapper<EnemyBrain> mEnemyBrain;
	protected ComponentMapper<EnemyBTree> mEnemyBTree;
	protected ComponentMapper<SBehaviour> mSBehaviour;

	@Wire
	Stage stage;
	Steering steering;

	VisWindow editorWindow;
	BehaviorTreeEditor<EnemyBrain> editor;

	private FileChooser saveAsFC;
	private FileChooser loadFC;
	private FileChooser saveTaskAsFC;
	private String treeToSave;
	private String taskToSave;
	private FileHandle saveFH;

	@Wire BTreeLoader loader;
	BehaviorTree<EnemyBrain> loaded;
	public BTEditor () {
		super(Aspect.all(EnemyBrain.class, EnemyBTree.class, BTWatcher.class).exclude(Dead.class));
	}

	@Override protected void initialize () {
		saveAsFC = new FileChooser(FileChooser.Mode.SAVE);
		saveAsFC.setDirectory(Gdx.files.getLocalStoragePath());
		saveAsFC.setListener(new FileChooserAdapter() {
			@Override public void selected (FileHandle file) {
				Gdx.app.log("", "save " + file.file().getAbsolutePath());
				saveFH = file;
				saveBT(treeToSave, saveFH);
			}
		});

		loadFC = new FileChooser(FileChooser.Mode.OPEN);
		loadFC.setDirectory(Gdx.files.getLocalStoragePath());
		loadFC.setListener(new FileChooserAdapter() {
			@Override public void selected (FileHandle file) {
				Gdx.app.log("", "load " + file.file().getAbsolutePath());
				loaded = loader.load(file);
			}
		});

		saveTaskAsFC = new FileChooser(FileChooser.Mode.SAVE);
		saveTaskAsFC.setDirectory(Gdx.files.getLocalStoragePath());
		saveTaskAsFC.setListener(new FileChooserAdapter() {
			@Override public void selected (FileHandle file) {
				Gdx.app.log("", "save tasl " + file.file().getAbsolutePath());
				saveBT(taskToSave, file);
			}
		});


		editor = new BehaviorTreeEditor<>(VisUI.getSkin(), VisUI.getSkin().getDrawable("white"));

		editor.setTaskInjector(new TaskLibrary.Injector<EnemyBrain>() {
			@Override public void inject (Task<EnemyBrain> task) {
				loader.injectTask(task);

			}
		});

		editor.addTaskClass("branch", Sequence.class);
		editor.addTaskClass("branch", Selector.class);
		editor.addTaskClass("branch", Parallel.class);
		editor.addTaskClass("branch", RandomSequence.class);
		editor.addTaskClass("branch", RandomSelector.class);

		editor.addTaskClass("decorator", AlwaysFail.class);
		editor.addTaskClass("decorator", AlwaysSucceed.class);
		editor.addTaskClass("decorator", Include.class);
		editor.addTaskClass("decorator", Invert.class);
		editor.addTaskClass("decorator", Random.class);
		editor.addTaskClass("decorator", Repeat.class);
		editor.addTaskClass("decorator", SemaphoreGuard.class);
		editor.addTaskClass("decorator", UntilFail.class);
		editor.addTaskClass("decorator", UntilSuccess.class);
		editor.addTaskClass("decorator", Wait.class);
		editor.addTaskClass("decorator", Success.class);
		editor.addTaskClass("decorator", Failure.class);

		editor.addTaskClass("actions", AttackTask.class);
		editor.addTaskClass("actions", AttackCoolDownTask.class);
		editor.addTaskClass("actions", PursueTask.class);
		editor.addTaskClass("actions", EvadeTask.class);
		editor.addTaskClass("actions", WanderTask.class);
		editor.addTaskClass("actions", StopSteeringTask.class);
		editor.addTaskClass("actions", ShootTask.class);
		editor.addTaskClass("actions", MeleeTask.class);

		editor.addTaskClass("conditions", HPAboveTask.class);
		editor.addTaskClass("conditions", InRangeTask.class);
		editor.addTaskClass("conditions", InAttackRangeTask.class);
		editor.addTaskClass("conditions", IsAliveTask.class);

		editor.addTaskClass("stack", FindGroupTask.class);
		editor.addTaskClass("stack", StackIsEmptyTask.class);
		editor.addTaskClass("stack", StackPopTask.class);
		editor.addTaskClass("stack", StackPushTask.class);
		editor.addTaskClass("stack", IsValueSet.class);

		editor.setPersist(new IPersist<EnemyBrain>() {
			@Override public void onSave (String tree) {
				Gdx.app.log(TAG, "save");
				if (saveFH == null) {
					onSaveAs(tree);
				} else {
					saveBT(tree, saveFH);
				}
			}

			@Override public void onSaveAs (String tree) {
				Gdx.app.log(TAG, "save as");
				treeToSave = tree;
				stage.addActor(saveAsFC.fadeIn());
			}

			@Override public void onLoad () {
				Gdx.app.log(TAG, "load");
				stage.addActor(loadFC.fadeIn());
			}

			@Override public void onSaveTaskAs (String tree) {
				Gdx.app.log(TAG, "save as");
				taskToSave = tree;
				stage.addActor(saveTaskAsFC.fadeIn());
			}
		});

		editorWindow = new VisWindow("Editor");
		editorWindow.setResizable(true);
		editorWindow.add(editor).expand().fillX().top();
		editorWindow.pack();

		if (editorWindow.getWidth() > stage.getWidth()) {
			editorWindow.setWidth(stage.getWidth());
		}
		if (editorWindow.getHeight() > stage.getHeight()) {
			editorWindow.setHeight(stage.getHeight());
		}
		editorWindow.addCloseButton();
		VisTextButton showEditor = new VisTextButton("EDITOR");
		stage.addActor(showEditor);
		showEditor.addListener(new ClickListener() {
			@Override public void clicked (InputEvent event, float x, float y) {
				if (editorWindow.getParent() != null) {
					editorWindow.fadeOut();
				} else {
					stage.addActor(editorWindow.fadeIn());
					editorWindow.centerWindow();
				}
			}
		});
	}

	private void saveBT(String tree, FileHandle file) {
		file.writeString(tree, false);
	}

	@Override protected void inserted (int entityId) {
		EnemyBrain brain = mEnemyBrain.get(entityId);
		EnemyBTree tree = mEnemyBTree.get(entityId);
		tree.tree.setObject(brain);
		editor.initialize(tree.tree, "");
	}

	@Override protected void process (int entityId) {
		EnemyBrain brain = mEnemyBrain.get(entityId);
		EnemyBTree tree = mEnemyBTree.get(entityId);

		if (loaded != null) {
			tree.tree.cancel();
			tree.tree = loaded;
			editor.initialize(tree.tree, "");
			loaded = null;
		}
		// TODO do we really want this steering in here?
		if (mSBehaviour.has(entityId))
			steering.process(entityId);
		// NOTE if we ever have shared trees we would need to set the object each time
		tree.tree.setObject(brain);
		// entity is excluded from normal updates if it is watched, so we cam manipulate the tree
		tree.tree.step();
	}

	@Override protected void end () {
		stage.act();
		stage.draw();
	}

	@Override protected void removed (int entityId) {
		EnemyBrain brain = mEnemyBrain.get(entityId);
		EnemyBTree tree = mEnemyBTree.get(entityId);
		editor.reset();
	}
}
