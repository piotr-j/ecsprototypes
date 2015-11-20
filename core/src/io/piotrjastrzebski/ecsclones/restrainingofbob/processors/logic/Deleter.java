package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.DeleteAfter;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.Projectile;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;

/**
 * Created by PiotrJ on 31/08/15.
 */
@Wire
public class Deleter extends IteratingSystem {
	protected ComponentMapper<DeleteAfter> mRemoveAfter;
	private ComponentMapper<Projectile> mProjectile;
	private ComponentMapper<EnemyBrain> mEnemyBrain;

	public Deleter () {
		super(Aspect.all(DeleteAfter.class));
	}

	@Override protected void process (int eid) {
		DeleteAfter after = mRemoveAfter.get(eid);
		after.timer += world.delta;
		if (after.timer > after.delay) {
			after.timer = 0;
			if (mProjectile.has(eid)) {
				Gdx.app.log("", "Delete bullet " + eid);
			} else if (mEnemyBrain.has(eid)) {
				Gdx.app.log("", "Delete enemy " + eid);
			} else {
				Gdx.app.log("", "Delete unknown " + eid);
			}
			world.delete(eid);
		}
	}
}
