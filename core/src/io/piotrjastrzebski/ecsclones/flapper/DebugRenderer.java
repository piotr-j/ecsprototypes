package io.piotrjastrzebski.ecsclones.flapper;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.piotrjastrzebski.ecsclones.base.components.DebugRender;
import io.piotrjastrzebski.ecsclones.base.components.Position;
import io.piotrjastrzebski.ecsclones.base.components.Size;

/**
 * Created by PiotrJ on 04/08/15.
 */
@Wire
public class DebugRenderer extends EntityProcessingSystem {
	@Wire(name = FlapperScreen.WIRE_GAME_CAM) OrthographicCamera camera;
	@Wire ShapeRenderer renderer;

	protected ComponentMapper<Position> mPosition;
	protected ComponentMapper<Size> mSize;
	protected ComponentMapper<DebugRender> mDebugRender;

	public DebugRenderer () {
		super(Aspect.all(Position.class, Size.class, DebugRender.class));
	}

	@Override protected void begin () {
		renderer.setProjectionMatrix(camera.combined);
		renderer.begin(ShapeRenderer.ShapeType.Line);
	}

	@Override protected void process (Entity e) {
		DebugRender debugRender = mDebugRender.get(e);
		renderer.setColor(debugRender.color);
		Position position = mPosition.get(e);
		Size size = mSize.get(e);
		switch (debugRender.type) {
		case DebugRender.TYPE_RECT:
			renderer.rect(position.pos.x, position.pos.y, size.width, size.height);
			break;
		case DebugRender.TYPE_CIRCLE:
			renderer.circle(position.pos.x, position.pos.y, size.width, 16);
			break;
		}
	}

	@Override protected void end () {
		renderer.end();
	}
}
