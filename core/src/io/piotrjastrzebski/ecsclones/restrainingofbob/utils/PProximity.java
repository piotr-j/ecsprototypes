package io.piotrjastrzebski.ecsclones.restrainingofbob.utils;

import com.badlogic.gdx.ai.steer.Proximity;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.physics.Physics;

/**
 * {@link Proximity<Vector2>} implementation for use with box2d with rectangular check
 *
 * Created by PiotrJ on 12/09/15.
 */
public class PProximity implements Proximity<Vector2>, QueryCallback {
	protected Steerable<Vector2> owner;
	protected World world;
	protected ProximityCallback<Vector2> callback;
	protected float radius = 1;
	private int neighbors;

	public void setWorld (World world) {
		this.world = world;
	}

	@Override public Steerable<Vector2> getOwner () {
		return owner;
	}

	public void setRadius (float radius) {
		this.radius = radius;
	}

	@Override public void setOwner (Steerable<Vector2> owner) {
		this.owner = owner;
	}

	@Override public int findNeighbors (ProximityCallback<Vector2> callback) {
		this.callback = callback;
		neighbors = 0;
		Vector2 p = owner.getPosition();
		world.QueryAABB(this, p.x - radius, p.y - radius, p.x + radius, p.y + radius);
		this.callback = null;
		return neighbors;
	}

	@Override
	public boolean reportFixture (Fixture fixture) {
		Steerable<Vector2> steerable = getSteerable(fixture);
		if (steerable != null && steerable != owner && interestedIn(steerable)) {
			if (callback.reportNeighbor(steerable)) neighbors++;
		}
		return true;
	}

	protected boolean interestedIn (Steerable<Vector2> steerable) {
		return true;
	}

	@SuppressWarnings("unchecked")
	protected Steerable<Vector2> getSteerable (Fixture fixture) {
		Physics.UserData userData = (Physics.UserData)fixture.getBody().getUserData();
		if (userData == null) return null;
		return userData.steerable;
	}
}
