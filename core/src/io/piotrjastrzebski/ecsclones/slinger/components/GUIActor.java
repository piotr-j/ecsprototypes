package io.piotrjastrzebski.ecsclones.slinger.components;

import com.artemis.PooledComponent;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by EvilEntity on 15/08/2015.
 */
public class GUIActor extends PooledComponent {
	public Actor actor;
	@Override protected void reset () {
		actor = null;
	}
}
