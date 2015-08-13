package io.piotrjastrzebski.ecsclones.flapper;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityEdit;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Rectangle;
import io.piotrjastrzebski.ecsclones.base.components.*;

/**
 * Created by PiotrJ on 04/08/15.
 */
@Wire
public class CollisionFinder extends EntityProcessingSystem {
	protected ComponentMapper<Collided> mCollided;
	protected ComponentMapper<Position> mPosition;
	protected ComponentMapper<Size> mSize;

	public CollisionFinder () {
		super(Aspect.all(Collider.class, Position.class, Size.class));
	}

	IntBag entities;

	@Override protected void begin () {
		entities = getSubscription().getEntities();
	}

	Rectangle eb = new Rectangle();
	Rectangle ob = new Rectangle();

	@Override protected void process (Entity e) {
		Position pos = mPosition.get(e);
		Size size = mSize.get(e);
		eb.set(pos.pos.x, pos.pos.y, size.width, size.height);
		EntityEdit ee = e.edit();
		for (int i = 0; i < entities.size(); i++) {
			Entity o = world.getEntity(entities.get(i));
			if (e.id == o.id)
				continue;
			Position oPos = mPosition.get(o);
			Size oSize = mSize.get(o);
			ob.set(oPos.pos.x, oPos.pos.y, oSize.width, oSize.height);
			if (eb.overlaps(ob)) {
				Collided ec = mCollided.getSafe(e);
				if (ec == null) {
					ec = ee.create(Collided.class);
				}
				Collided oc = mCollided.getSafe(o, true);
				if (oc == null) {
					oc = o.edit().create(Collided.class);
				}
				ec.with.add(o.id);
				oc.with.add(e.id);
			}
		}
	}
}
