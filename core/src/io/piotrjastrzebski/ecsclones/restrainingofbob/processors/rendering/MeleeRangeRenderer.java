package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.rendering;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import io.piotrjastrzebski.ecsclones.base.GameScreen;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.CircleBounds;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.Health;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.RectBounds;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.EnemyBrain;

/**
 * Created by PiotrJ on 29/08/15.
 */
@Wire
public class MeleeRangeRenderer extends EntityProcessingSystem {
	@Wire(name = GameScreen.WIRE_GAME_CAM) OrthographicCamera camera;
	@Wire ShapeRenderer renderer;

	protected ComponentMapper<RectBounds> mSize;
	protected ComponentMapper<CircleBounds> mRadius;

	public MeleeRangeRenderer () {
		super(Aspect.all(EnemyBrain.class, CircleBounds.class));
	}

	@Override protected void begin () {
		renderer.setProjectionMatrix(camera.combined);
		renderer.begin(ShapeRenderer.ShapeType.Line);
	}

	@Override protected void process (Entity e) {
		float x, y;
		// TODO need actual range from somewhere...
		float r;
		x = y = r = 0;
		CircleBounds circle = mRadius.getSafe(e);
		if (circle != null) {
			x = circle.bounds.x;
			y = circle.bounds.y;
			r = circle.radius;
		}

		renderer.setColor(Color.CYAN);
		renderer.circle(x, y, .75f, 12);
	}

	@Override protected void end () {
		renderer.end();
	}
}