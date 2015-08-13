package io.piotrjastrzebski.ecsclones.flapper;

import com.artemis.*;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import io.piotrjastrzebski.ecsclones.base.components.*;
import io.piotrjastrzebski.ecsclones.flapper.components.Flapper;

/**
 * Created by PiotrJ on 08/08/15.
 */
public class FlapperRespawner extends EntityProcessingSystem {

	public FlapperRespawner () {
		super(Aspect.all(Flapper.class));
	}

	@Override protected void initialize () {
		super.initialize();
		createFlapper();
	}

	@Override protected void removed (Entity e) {
		// TODO better way of clearing the world
		EntitySubscription all = world.getManager(AspectSubscriptionManager.class).get(Aspect.all());
		IntBag entities = all.getEntities();
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = world.getEntity(entities.get(i));
			if (entity != null) {
				entity.deleteFromWorld();
			} else {
				Gdx.app.log("", "null entity " + entities.get(i));
			}
		}
		createFlapper();
	}

	private void createFlapper () {
		Entity entity = world.createEntity();
		EntityEdit edit = entity.edit();
		Flapper flapper = edit.create(Flapper.class);
		flapper.jumpAcc.set(0, 15);
		flapper.forwardAcc.set(20, 0);

		Position position = edit.create(Position.class);
		position.pos.set(-FlapperScreen.VP_WIDTH/3, 0);

		Size size = edit.create(Size.class);
		size.width = 1;
		size.height = 1;

		Movement movement = edit.create(Movement.class);
		movement.friction = -0.1f;

		Gravity gravity = edit.create(Gravity.class);
		gravity.acc.set(0, -1);

		DebugRender debugRender = edit.create(DebugRender.class);
		debugRender.color.set(Color.GREEN);

		edit.create(Collider.class);
	}

	@Override protected void process (Entity e) {
		// we only care about removed
	}
}
