package io.piotrjastrzebski.ecsclones.flapper;

import com.artemis.*;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import io.piotrjastrzebski.ecsclones.base.components.*;
import io.piotrjastrzebski.ecsclones.flapper.components.Flapper;

/**
 * Created by PiotrJ on 08/08/15.
 */
@Wire
public class FlapperFollower extends EntityProcessingSystem {
	@Wire
	OrthographicCamera camera;
	protected ComponentMapper<Position> mPosition;
	protected ComponentMapper<Size> mSize;

	public FlapperFollower () {
		super(Aspect.all(Flapper.class, Position.class, Size.class));
	}

	@Override protected void process (Entity e) {
		Position position = mPosition.get(e);
		Size size = mSize.get(e);
		camera.position.set(position.pos.x + size.width /2 ,0, 0);
		camera.update();
	}
}
