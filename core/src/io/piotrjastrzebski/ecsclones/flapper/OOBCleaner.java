package io.piotrjastrzebski.ecsclones.flapper;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import io.piotrjastrzebski.ecsclones.base.components.Collided;
import io.piotrjastrzebski.ecsclones.base.components.Position;
import io.piotrjastrzebski.ecsclones.base.components.Size;

/**
 * Created by PiotrJ on 04/08/15.
 */
@Wire
public class OOBCleaner extends EntityProcessingSystem {
	protected ComponentMapper<Position> mPosition;
	protected ComponentMapper<Size> mSize;

	@Wire(name = FlapperScreen.WIRE_GAME_CAM) OrthographicCamera camera;
	@Wire(name = FlapperScreen.WIRE_GAME_VP) ExtendViewport viewport;

	public OOBCleaner () {
		super(Aspect.all(Position.class, Size.class));
	}

	float margin = 5;
	Rectangle camBounds = new Rectangle();
	@Override protected void begin () {
		camBounds.set(
			camera.position.x - viewport.getWorldWidth() / 2 - margin,
			camera.position.y - viewport.getWorldHeight() / 2 - margin,
			viewport.getWorldWidth() + margin * 2,
			viewport.getWorldHeight() + margin * 2
		);
	}

	Rectangle eBounds = new Rectangle();
	@Override protected void process (Entity e) {
		Position position = mPosition.get(e);
		Size size = mSize.get(e);
		eBounds.set(position.pos.x, position.pos.y, size.width, size.height);

		if (!camBounds.overlaps(eBounds)) {
			e.deleteFromWorld();
		}
	}
}
