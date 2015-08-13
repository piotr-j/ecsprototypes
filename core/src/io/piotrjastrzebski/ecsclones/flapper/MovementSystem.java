package io.piotrjastrzebski.ecsclones.flapper;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import io.piotrjastrzebski.ecsclones.base.components.Movement;
import io.piotrjastrzebski.ecsclones.base.components.Position;

/**
 * Created by PiotrJ on 04/08/15.
 */
@Wire
public class MovementSystem extends EntityProcessingSystem {
	private final static String TAG = MovementSystem.class.getSimpleName();

	protected ComponentMapper<Movement> mMovement;
	protected ComponentMapper<Position> mPosition;

	public MovementSystem () {
		super(Aspect.all(Movement.class, Position.class));
	}

	Vector2 tmp = new Vector2();
	@Override protected void process (Entity e) {
		Position position = mPosition.get(e);
		Movement movement = mMovement.get(e);

		// apply friction
		movement.vel.add(tmp.set(movement.vel).scl(movement.friction));

		// calculate new position
		tmp.set(movement.acc).scl(0.5f * world.delta * world.delta);
		position.pos.add(tmp);
		tmp.set(movement.vel).scl(world.delta);
		position.pos.add(tmp);

		// calculate new velocity
		tmp.set(movement.acc).scl(world.delta);
		movement.vel.add(tmp);
	}
}
