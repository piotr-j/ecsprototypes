package io.piotrjastrzebski.ecsclones.slinger.systems.physics;

import com.artemis.*;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.physics.box2d.World;
import io.piotrjastrzebski.ecsclones.slinger.components.WorldDef;

/**
 * Created by EvilEntity on 15/08/2015.
 */
@Wire
public class Physics extends EntitySystem {
	private final static String TAG = Physics.class.getSimpleName();
	private ComponentMapper<WorldDef> mWorldDef;
	private World box2d;
	@Wire PhysicsContacts contacts;
	public Physics () {
		super(Aspect.all(WorldDef.class));
	}

	private int velIters;
	private int posIters;
	private float step;
	@Override protected void inserted (Entity e) {
		if (box2d != null) {
			throw new IllegalStateException(TAG + " there cant be more than one world at a time!");
		}
		WorldDef def = mWorldDef.get(e);
		box2d = new World(def.gravity, def.sleep);
		box2d.setContactListener(contacts);
		velIters = def.velIters;
		posIters = def.posIters;
		step = def.step;
	}

	@Override protected void processSystem () {
		if (box2d == null) return;
		// TODO proper fixed step with interpolation and stuff
		box2d.step(step, velIters, posIters);
	}

	@Override protected void removed (Entity e) {
		// todo do this properly
		box2d.dispose();
		box2d = null;
	}

	public World getWorld () {
		return box2d;
	}

	public static class UserData {
		public int category;
		public int entity;

		public UserData (Entity entity) {
			set(entity);
		}

		public UserData set (Entity entity) {
			this.entity = entity.id;
			return this;
		}

		/**
		 * Called when owner of this UserData collided with something, other may be null
		 * WARNING this is called from contact listener, box2d objects cant be modified in here
		 */
		public void onContact (UserData other) {

		}

		/**
		 * Called when owner of this UserData stopped colliding with something, other may be null
		 * WARNING this is called from contact listener, box2d objects cant be modified in here
		 * This should be called even if owner entity gets deletedFromWorld() in onContact
		 */
		public void onEndContact (UserData other) {

		}

		/**
		 * Called after collision was solved, other may be null. Strength is impulse required to solve the collision
		 * WARNING this is called from contact listener, box2d objects cant be modified in here
		 * This should be called even if owner entity gets deletedFromWorld() in onContact
		 */
		public void onPostSolve (UserData userData, float strength) {

		}
	}

	public static class Category {
		public final static short DEFAULT = 0;
		public final static short BOUNDARY = 1;
		public final static short PROJECTILE = 1 << 1;
		public final static short BLOCK = 1 << 2;
		public final static short TARGET = 1 << 3;
	}
}
