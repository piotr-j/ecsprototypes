package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.status.Dead;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.status.Health;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.status.HitBy;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.status.Invulnerable;

/**
 * Created by PiotrJ on 29/08/15.
 */
@Wire
public class HitBySystem extends EntityProcessingSystem {
	protected ComponentMapper<HitBy> mHitBy;
	protected ComponentMapper<Health> mHealth;
	protected ComponentMapper<Invulnerable> mInvulnerable;

	public HitBySystem () {
		super(Aspect.all(HitBy.class, Health.class).exclude(Dead.class));
	}

	@Override protected void process (Entity e) {
		if (!mInvulnerable.has(e)) {
			HitBy hitBy = mHitBy.get(e);
			Health health = mHealth.get(e);
			health.hp -= hitBy.dmg;
		}
		// TODO apply some status base on type or whatever
		e.edit().remove(HitBy.class);
	}
}
