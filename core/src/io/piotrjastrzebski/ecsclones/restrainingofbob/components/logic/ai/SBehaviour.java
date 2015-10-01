package io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai;

import com.artemis.PooledComponent;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by PiotrJ on 26/08/15.
 */
public class SBehaviour extends PooledComponent {
	public SteeringBehavior<Vector2> behaviour;
	public int size;
	public int target;

	public SBehaviour () {
		reset();
	}

	@Override protected void reset () {
		behaviour = null;
		size = 1;
		target = -1;
	}
}
