package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.physics;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.PSteerable;

/**
 * Created by PiotrJ on 22/08/15.
 */
@Wire
public class Physics extends BaseSystem {
	private final static String TAG = Physics.class.getSimpleName();
	private World box2d;

	@Wire PhysicsContacts contacts;

	private int velIters;
	private int posIters;
	private float step;

	@Override protected void initialize () {
		super.initialize();
		box2d = new World(new Vector2(), true);
		box2d.setContactListener(contacts);
		velIters = 6;
		posIters = 4;
		step = 1f/60f;
	}

	@Override protected void processSystem () {
		if (box2d == null) return;
		// TODO proper fixed step with interpolation and stuff
		box2d.step(step, velIters, posIters);
	}

	public World getWorld () {
		return box2d;
	}

	public static class UserData {
		public short category;
		public int entity;
		public PSteerable steerable;

		public UserData () {}

		public UserData (Entity entity) {
			set(entity);
		}

		public UserData (Entity e, short category) {
			set(e);
			this.category = category;
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

	@Override protected void dispose () {
		box2d.dispose();
	}
}
