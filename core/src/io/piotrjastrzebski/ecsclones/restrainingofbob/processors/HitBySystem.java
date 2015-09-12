package io.piotrjastrzebski.ecsclones.restrainingofbob.processors;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.*;

/**
 * Created by PiotrJ on 29/08/15.
 */
@Wire
public class HitBySystem extends EntityProcessingSystem {
	protected ComponentMapper<HitBy> mHitBy;
	protected ComponentMapper<Health> mHealth;

	public HitBySystem () {
		super(Aspect.all(HitBy.class, Health.class).exclude(Invulnerable.class, Dead.class));
	}

	@Override protected void process (Entity e) {
		HitBy hitBy = mHitBy.get(e);
		Health health = mHealth.get(e);
		health.hp -= hitBy.dmg;
		// TODO apply some status base on type or whatever
		e.edit().remove(HitBy.class);
	}
}
