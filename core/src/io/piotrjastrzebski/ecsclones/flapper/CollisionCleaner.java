package io.piotrjastrzebski.ecsclones.flapper;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import io.piotrjastrzebski.ecsclones.base.components.Collided;

/**
 * Created by PiotrJ on 04/08/15.
 */
@Wire
public class CollisionCleaner extends EntityProcessingSystem {
	protected ComponentMapper<Collided> mCollided;

	public CollisionCleaner () {
		super(Aspect.all(Collided.class));
	}

	@Override protected void inserted (int eid) {
		world.getEntity(eid).edit().remove(Collided.class);
	}

	@Override protected void process (Entity e) {

	}
}
