
package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.scenes.scene2d.Stage;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.BTWatcher;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBTree;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.SBehaviour;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.status.Dead;

/**
 *
 * Created by PiotrJ on 31/08/15.
 */
@Wire
public class BTEditor extends IteratingSystem {
	protected ComponentMapper<EnemyBrain> mEnemyBrain;
	protected ComponentMapper<EnemyBTree> mEnemyBTree;
	protected ComponentMapper<SBehaviour> mSBehaviour;

	@Wire
	Stage stage;
	Steering steering;

	public BTEditor () {
		super(Aspect.all(EnemyBrain.class, EnemyBTree.class, BTWatcher.class).exclude(Dead.class));
	}

	@Override protected void initialize () {

	}

	@Override protected void inserted (int entityId) {
		EnemyBrain brain = mEnemyBrain.get(entityId);
		EnemyBTree tree = mEnemyBTree.get(entityId);
		tree.tree.setObject(brain);
	}

	@Override protected void process (int entityId) {
		EnemyBrain brain = mEnemyBrain.get(entityId);
		EnemyBTree tree = mEnemyBTree.get(entityId);
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

	}
}
