package io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic;

import com.artemis.Component;
import com.artemis.PooledComponent;
import com.badlogic.gdx.utils.Array;

/**
 * Created by PiotrJ on 26/08/15.
 */
public class RemoveAfter extends PooledComponent {
	public float delay;
	public float timer;
	public Array<Class<? extends Component>> clses = new Array<>();

	public RemoveAfter setDelay (float delay) {
		this.delay = delay;
		return this;
	}

	public RemoveAfter add (Component comp) {
		this.clses.add(comp.getClass());
		return this;
	}

	public RemoveAfter add (Class<? extends Component> cls) {
		this.clses.add(cls);
		return this;
	}

	@Override protected void reset () {
		delay = 0;
		timer = 0;
		clses.clear();
	}
}
