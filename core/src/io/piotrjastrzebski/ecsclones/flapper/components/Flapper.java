package io.piotrjastrzebski.ecsclones.flapper.components;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by PiotrJ on 04/08/15.
 */
public class Flapper extends PooledComponent {
	public int score;
	public Vector2 jumpAcc = new Vector2();
	public Vector2 forwardAcc = new Vector2();

	@Override protected void reset () {
		score = 0;
		jumpAcc.setZero();
		forwardAcc.setZero();
	}
}
