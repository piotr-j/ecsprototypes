package io.piotrjastrzebski.ecsclones.slinger.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by EvilEntity on 15/08/2015.
 */
public class WorldDef extends Component {
	public Vector2 gravity = new Vector2(0, -0.98f);
	public boolean sleep = true;
	public float step = 1/30f;
	public int velIters = 6;
	public int posIters = 4;
}
