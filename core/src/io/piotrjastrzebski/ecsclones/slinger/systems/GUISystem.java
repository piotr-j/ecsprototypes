package io.piotrjastrzebski.ecsclones.slinger.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.scenes.scene2d.Stage;
import io.piotrjastrzebski.ecsclones.slinger.components.GUIActor;

/**
 * Created by EvilEntity on 15/08/2015.
 */
@Wire
public class GUISystem extends EntitySystem {
	private ComponentMapper<GUIActor> mGUIActor;
	@Wire Stage stage;

	public GUISystem () {
		super(Aspect.all(GUIActor.class));
	}

	@Override protected void inserted (int eid) {
		GUIActor guiActor = mGUIActor.get(eid);
		stage.addActor(guiActor.actor);
	}

	@Override protected void processSystem () {
		stage.act(world.delta);
		stage.draw();
	}

	@Override protected void removed (int eid) {
		GUIActor guiActor = mGUIActor.get(eid);
		guiActor.actor.remove();
	}
}
