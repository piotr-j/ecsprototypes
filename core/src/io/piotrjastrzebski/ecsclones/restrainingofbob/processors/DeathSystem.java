package io.piotrjastrzebski.ecsclones.restrainingofbob.processors;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Color;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.Dead;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.Health;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.HitBy;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.Invulnerable;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.RemoveAfter;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.rendering.DebugTint;

/**
 * Created by PiotrJ on 29/08/15.
 */
@Wire
public class DeathSystem extends EntityProcessingSystem {
	protected ComponentMapper<Health> mHealth;
	protected ComponentMapper<DebugTint> mDebugTint;

	public DeathSystem () {
		super(Aspect.all(Health.class).exclude(Invulnerable.class, Dead.class));
	}

	@Override protected void process (Entity e) {
		Health health = mHealth.get(e);
		if (health.hp <= 0) {
			e.edit().create(Dead.class);
			e.edit().create(RemoveAfter.class).setDelay(3);
			if (mDebugTint.has(e)) {
				mDebugTint.get(e).color.set(Color.BROWN);
			}
		}
	}
}
