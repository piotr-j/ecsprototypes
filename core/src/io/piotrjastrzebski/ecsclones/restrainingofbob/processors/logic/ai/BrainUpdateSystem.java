package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.status.Health;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;

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
		enemyBrain.maxHP = health.maxHp;
		enemyBrain.hp = health.hp;
	}
}
