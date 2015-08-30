package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.rendering;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.piotrjastrzebski.ecsclones.base.GameScreen;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.RectBounds;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.CircleBounds;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.rendering.DebugTint;

/**
 * Created by PiotrJ on 29/08/15.
 */
@Wire
public class DebugRenderer extends EntityProcessingSystem {
	@Wire(name = GameScreen.WIRE_GAME_CAM) OrthographicCamera camera;
	@Wire ShapeRenderer renderer;

	protected ComponentMapper<RectBounds> mSize;
	protected ComponentMapper<CircleBounds> mRadius;
	protected ComponentMapper<DebugTint> mTint;

	public DebugRenderer () {
		super(Aspect.all(DebugTint.class).one(RectBounds.class, CircleBounds.class));
	}

	@Override protected void begin () {
		renderer.setProjectionMatrix(camera.combined);
		renderer.begin(ShapeRenderer.ShapeType.Line);
	}

	@Override protected void process (Entity e) {
		DebugTint debugTint = mTint.get(e);
		renderer.setColor(debugTint.color);

		CircleBounds circle = mRadius.getSafe(e);
		if (circle != null) {
			renderer.circle(circle.bounds.x, circle.bounds.y, circle.radius, 16);
		}

		RectBounds rect = mSize.getSafe(e);
		if (rect != null) {
			renderer.rect(rect.bounds.x, rect.bounds.y, rect.bounds.width, rect.bounds.height);
		}
	}

	@Override protected void end () {
		renderer.end();
	}
}
