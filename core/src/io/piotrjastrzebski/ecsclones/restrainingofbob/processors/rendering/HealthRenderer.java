package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.rendering;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.piotrjastrzebski.ecsclones.base.GameScreen;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.*;

/**
 * Created by PiotrJ on 29/08/15.
 */
@Wire
public class HealthRenderer extends EntityProcessingSystem {
	@Wire(name = GameScreen.WIRE_GAME_CAM) OrthographicCamera camera;
	@Wire ShapeRenderer renderer;

	protected ComponentMapper<RectBounds> mSize;
	protected ComponentMapper<CircleBounds> mRadius;
	protected ComponentMapper<Health> mHealth;

	public HealthRenderer () {
		super(Aspect.all(Health.class).one(RectBounds.class, CircleBounds.class));
	}

	@Override protected void begin () {
		renderer.setProjectionMatrix(camera.combined);
		renderer.begin(ShapeRenderer.ShapeType.Filled);
	}

	@Override protected void process (Entity e) {
		float x, y;
		float w, h;
		x = y = w = h = 0;
		CircleBounds circle = mRadius.getSafe(e);
		if (circle != null) {
			x = circle.bounds.x;
			y = circle.bounds.y;
			w = h = circle.radius * 2;
		}

		RectBounds rect = mSize.getSafe(e);
		if (rect != null) {
			x = rect.bounds.x;
			y = rect.bounds.y;
			w = rect.width;
			h = rect.height;
		}

		Health health = mHealth.get(e);
		float a = health.hp/health.maxHp;
		renderer.setColor(Color.GRAY);
		renderer.rect(x - w / 2, y - h * .85f, w, h * 0.25f);
		if (a >= 0) {
			renderer.setColor(1 - a, a, 0, 1);
			renderer.rect(x - w / 2, y - h * .85f, w * a, h * 0.25f);
		}
	}

	@Override protected void end () {
		renderer.end();
	}
}
