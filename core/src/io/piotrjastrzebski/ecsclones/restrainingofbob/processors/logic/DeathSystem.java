package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.status.Dead;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.status.Health;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.status.Invulnerable;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.DeleteAfter;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.rendering.DebugTint;

/**
 * Created by PiotrJ on 29/08/15.
 */
@Wire
public class DeathSystem extends IteratingSystem {
	protected ComponentMapper<Dead> mDead;
	protected ComponentMapper<DeleteAfter> mDeleteAfter;
	protected ComponentMapper<Health> mHealth;
	protected ComponentMapper<DebugTint> mDebugTint;

	public DeathSystem () {
		super(Aspect.all(Health.class).exclude(Invulnerable.class, Dead.class));
	}

	@Override protected void process (int eid) {
		Health health = mHealth.get(eid);
		if (health.hp > 0) return;
		mDead.create(eid);
		mDeleteAfter.create(eid).setDelay(.5f);
		if (mDebugTint.has(eid)) {
			mDebugTint.get(eid).color.set(Color.BROWN);
		}
	}
}
