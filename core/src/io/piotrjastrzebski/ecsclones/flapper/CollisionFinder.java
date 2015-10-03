package io.piotrjastrzebski.ecsclones.flapper;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityEdit;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.systems.IteratingSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Rectangle;
import io.piotrjastrzebski.ecsclones.base.components.*;

/**
 * Created by PiotrJ on 04/08/15.
 */
@Wire
public class CollisionFinder extends IteratingSystem {
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

	@Override protected void process (int eid) {
		Position pos = mPosition.get(eid);
		Size size = mSize.get(eid);
		eb.set(pos.pos.x, pos.pos.y, size.width, size.height);
		for (int i = 0; i < entities.size(); i++) {
			int oid = entities.get(i);
			if (eid == oid)
				continue;
			Position oPos = mPosition.get(oid);
			Size oSize = mSize.get(oid);
			ob.set(oPos.pos.x, oPos.pos.y, oSize.width, oSize.height);
			if (eb.overlaps(ob)) {
				Collided ec = mCollided.create(eid);
				Collided oc = mCollided.create(oid);
				ec.with.add(oid);
				oc.with.add(eid);
			}
		}
	}
}
