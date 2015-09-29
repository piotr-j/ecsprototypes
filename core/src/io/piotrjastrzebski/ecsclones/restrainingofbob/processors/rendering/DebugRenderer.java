package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.rendering;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.piotrjastrzebski.ecsclones.base.GameScreen;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.AimDirection;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.MoveFacing;
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
	protected ComponentMapper<MoveFacing> mMoveFacing;
	protected ComponentMapper<AimDirection> mAimFacing;

	public DebugRenderer () {
		super(Aspect.all(DebugTint.class).one(RectBounds.class, CircleBounds.class));
	}

	@Override protected void begin () {
		renderer.setProjectionMatrix(camera.combined);
		renderer.begin(ShapeRenderer.ShapeType.Filled);
	}

	Vector2 fv = new Vector2();
	@Override protected void process (Entity e) {
		DebugTint debugTint = mTint.get(e);
		renderer.setColor(debugTint.color);

		float x, y;
		float w, h;
		x = y = w = h = 0;
		CircleBounds circle = mRadius.getSafe(e);
		if (circle != null) {
			renderer.circle(circle.bounds.x, circle.bounds.y, circle.radius, 16);
			x = circle.bounds.x;
			y = circle.bounds.y;
			w = h = circle.radius;
		}

		RectBounds rect = mSize.getSafe(e);
		if (rect != null) {
			renderer.rect(rect.bounds.x, rect.bounds.y, rect.bounds.width, rect.bounds.height);
			x = rect.bounds.x;
			y = rect.bounds.y;
			w = rect.width;
			h = rect.height;
		}

		if (mMoveFacing.has(e)) {
			MoveFacing facing = mMoveFacing.get(e);
			fv.set(1, 0).setAngle(facing.dir.angle);
			renderer.setColor(Color.BLACK);
			float lw = Math.max(w, h)/5f;
			renderer.rectLine(x, y, x + w * fv.x/2, y + h * fv.y/2, lw);
		}
		if (mAimFacing.has(e)) {
			AimDirection facing = mAimFacing.get(e);
			fv.set(1, 0).setAngle(facing.angle);
			renderer.setColor(Color.BLACK);
			float lw = Math.max(w, h)/5f;
			renderer.rectLine(x + w * fv.x/2, y + h * fv.y/2, x + w * fv.x, y + h * fv.y, lw);
		}
	}

	@Override protected void end () {
		renderer.end();
	}
}
