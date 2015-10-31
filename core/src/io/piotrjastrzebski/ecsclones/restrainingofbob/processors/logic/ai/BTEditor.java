
package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.branch.*;
import com.badlogic.gdx.ai.btree.decorator.*;
import com.badlogic.gdx.ai.btree.leaf.Failure;
import com.badlogic.gdx.ai.btree.leaf.Success;
import com.badlogic.gdx.ai.btree.leaf.Wait;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeParser;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.SerializationException;
import com.badlogic.gdx.utils.StreamUtils;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import io.piotrjastrzebski.ecsclones.restrainingofbob.btedit.BehaviorTreeEditor;
import io.piotrjastrzebski.ecsclones.restrainingofbob.btedit.IPersist;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.BTWatcher;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBTree;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.SBehaviour;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.status.Dead;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.actions.*;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.conditions.HPAboveTask;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.conditions.InRangeTask;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.conditions.IsAliveTask;

import java.io.Reader;

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
	private String treeToSave;
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


		editor = new BehaviorTreeEditor<>(VisUI.getSkin(), VisUI.getSkin().getDrawable("white"));

		editor.addTaskClass(Sequence.class);
		editor.addTaskClass(RandomSequence.class);
		editor.addTaskClass(Selector.class);
		editor.addTaskClass(RandomSelector.class);
		editor.addTaskClass(Parallel.class);

		editor.addTaskClass(AlwaysFail.class);
		editor.addTaskClass(AlwaysSucceed.class);
		editor.addTaskClass(Include.class);
		editor.addTaskClass(Invert.class);
		editor.addTaskClass(Random.class);
		editor.addTaskClass(Repeat.class);
		editor.addTaskClass(SemaphoreGuard.class);
		editor.addTaskClass(UntilFail.class);
		editor.addTaskClass(UntilSuccess.class);
		editor.addTaskClass(Wait.class);
		editor.addTaskClass(Success.class);
		editor.addTaskClass(Failure.class);

		editor.addTaskClass(EvadeTask.class);
		editor.addTaskClass(MeleeTask.class);
		editor.addTaskClass(PursueTask.class);
		editor.addTaskClass(ShootTask.class);
		editor.addTaskClass(StopSteeringTask.class);
		editor.addTaskClass(WanderTask.class);

		editor.addTaskClass(HPAboveTask.class);
		editor.addTaskClass(InRangeTask.class);
		editor.addTaskClass(IsAliveTask.class);

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
		});

		editorWindow = new VisWindow("Editor");
		editorWindow.setResizable(true);
		editorWindow.add(editor).expand().fillX();
		editorWindow.pack();

		stage.addActor(editorWindow);
		editorWindow.centerWindow();
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
