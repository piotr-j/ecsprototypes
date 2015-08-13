package io.piotrjastrzebski.ecsclones.flapper;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Vector2;
import io.piotrjastrzebski.ecsclones.base.components.Gravity;
import io.piotrjastrzebski.ecsclones.base.components.Movement;
import io.piotrjastrzebski.ecsclones.base.components.Position;

/**
 * Created by PiotrJ on 04/08/15.
 */
@Wire
public class GravitySystem extends EntityProcessingSystem {
	protected ComponentMapper<Movement> mMovement;
	protected ComponentMapper<Gravity> mGravity;

	public GravitySystem () {
		super(Aspect.all(Movement.class, Gravity.class));
	}

	Vector2 tmp = new Vector2();
	@Override protected void process (Entity e) {
		Movement movement = mMovement.get(e);
		Gravity gravity = mGravity.get(e);
		movement.acc.add(gravity.acc);
	}
}
