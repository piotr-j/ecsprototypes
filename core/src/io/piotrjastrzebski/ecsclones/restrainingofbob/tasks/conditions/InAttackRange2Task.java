package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.conditions;

import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import io.piotrjastrzebski.bteditor.core.TaskComment;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.Meleer;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.Shooter;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.Finder;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai.storage.IntStorage;
import io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.base.BaseTask;

/**
 * Created by PiotrJ on 19/08/15.
 */
@Wire(injectInherited = true)
public class InAttackRange2Task extends BaseTask implements TaskComment {
	private final static String TAG = InAttackRange2Task.class.getSimpleName();

	@TaskAttribute(required=true)
	public String varName;

	Finder finder;
	private ComponentMapper<Shooter> mShooter;
	private ComponentMapper<Meleer> mMeleer;

	@Override public Status execute() {
		EnemyBrain brain = getObject();
		IntStorage storage = brain.getIntStorage();
		if (!storage.hasValue(varName)) return Status.FAILED;
		float range = 0;
		// lets assume for now that enemy can be only one of these at a time
		// this probably should be stored in storage
		if (mMeleer.has(brain.id)) {
			range = mMeleer.get(brain.id).range;
		} else if (mShooter.has(brain.id)) {
			range = mShooter.get(brain.id).range;
		}
		if (finder.overlaps(brain.id, storage.getValue(varName), range)) {
			return Status.SUCCEEDED;
		} else {
			return Status.FAILED;
		}
	}

	@Override protected Task<EnemyBrain> copyTo (Task<EnemyBrain> task) {
		InAttackRange2Task range = (InAttackRange2Task)task;
		range.varName = varName;
		range.finder = finder;
		range.mMeleer = mMeleer;
		range.mShooter = mShooter;
		return super.copyTo(task);
	}

	@Override public String getComment () {
		return "";
	}
}
