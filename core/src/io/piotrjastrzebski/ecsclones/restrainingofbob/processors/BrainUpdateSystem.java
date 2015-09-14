package io.piotrjastrzebski.ecsclones.restrainingofbob.processors;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.Dead;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.Health;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.HitBy;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.Invulnerable;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.EnemyBrain;

/**
 * Created by PiotrJ on 29/08/15.
 */
@Wire
public class BrainUpdateSystem extends EntityProcessingSystem {
	protected ComponentMapper<EnemyBrain> mEnemyBrain;
	protected ComponentMapper<Health> mHealth;

	public BrainUpdateSystem () {
		super(Aspect.all(EnemyBrain.class, Health.class));
	}

	@Override protected void process (Entity e) {
		EnemyBrain enemyBrain = mEnemyBrain.get(e);
		Health health = mHealth.get(e);
		enemyBrain.hp = health.hp;
	}
}
