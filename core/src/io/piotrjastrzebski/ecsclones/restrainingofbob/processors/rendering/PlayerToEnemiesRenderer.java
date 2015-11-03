package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.rendering;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import io.piotrjastrzebski.ecsclones.base.GameScreen;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.Player;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai.EnemyBrain;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.CircleBounds;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.RectBounds;

/**
 * Created by EvilEntity on 03/11/2015.
 */
public class PlayerToEnemiesRenderer extends EntityProcessingSystem {
	@Wire(name = GameScreen.WIRE_GAME_CAM) OrthographicCamera camera;
	@Wire(name = GameScreen.WIRE_GAME_VP) ExtendViewport viewport;
	@Wire ShapeRenderer renderer;

	protected ComponentMapper<RectBounds> mSize;
	protected ComponentMapper<CircleBounds> mRadius;

	public PlayerToEnemiesRenderer () {
		super(Aspect.all(EnemyBrain.class).one(RectBounds.class, CircleBounds.class));
	}
	EntitySubscription players;
	@Override protected void initialize () {
		super.initialize();
		players = world.getAspectSubscriptionManager().get(Aspect.all(Player.class));
	}

	boolean hasPlayer;
	float px, py;
	Rect viewBounds = new Rect();
	@Override protected void begin () {
		IntBag entities = players.getEntities();
		if (entities.size() == 0) {
			hasPlayer = false;
		} else {
			hasPlayer = true;
			CircleBounds circle = mRadius.getSafe(entities.get(0));
			if (circle != null) {
				px = circle.bounds.x;
				py = circle.bounds.y;
			}
		}
		viewBounds.set(
				camera.position.x - camera.viewportWidth / 2 - .5f,
				camera.position.y - camera.viewportHeight / 2 - .5f,
				camera.position.x + camera.viewportWidth / 2 + .5f,
				camera.position.y + camera.viewportHeight / 2 + .5f
		);
		renderer.setProjectionMatrix(camera.combined);
		renderer.begin(ShapeRenderer.ShapeType.Filled);
		renderer.setColor(.5f, .5f, .5f, .5f);
	}

	Vector2 tmp = new Vector2();
	Vector2 perp = new Vector2();
	@Override protected void process (Entity e) {
		if (!hasPlayer) return;
		float x, y;
		x = y = 0;
		CircleBounds circle = mRadius.getSafe(e);
		if (circle != null) {
			x = circle.bounds.x;
			y = circle.bounds.y;
		}

		RectBounds rect = mSize.getSafe(e);
		if (rect != null) {
			x = rect.bounds.x;
			y = rect.bounds.y;
		}

		if (!(viewBounds.contains(x, y))) {
			tmp.set(x, y).sub(px, py);
			tmp.nor();
			perp.set(-tmp.y, tmp.x);
//			renderer.triangle(
//					px + perp.x * .2f + tmp.x * 9.25f, py + perp.y * .2f  + tmp.y * 9.25f,
//					px - perp.x * .2f  + tmp.x * 9.25f, py - perp.y * .2f  + tmp.y * 9.25f,
//					px + tmp.x * 10, py + tmp.y * 10
//			);
			renderer.triangle(
					px + perp.x * .25f + tmp.x * 9.25f, py + perp.y * .25f  + tmp.y * 9.25f,
					px + tmp.x * 9.4f, py + tmp.y * 9.4f,
					px + tmp.x * 10, py + tmp.y * 10
			);
			renderer.triangle(
					px - perp.x * .25f  + tmp.x * 9.25f, py - perp.y * .25f  + tmp.y * 9.25f,
					px + tmp.x * 9.4f, py + tmp.y * 9.4f,
					px + tmp.x * 10, py + tmp.y * 10
			);
		}
	}

	public class Rect {
		public float x1;
		public float y1;
		public float x2;
		public float y2;

		public void set (float x1, float y1, float x2, float y2) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}

		public boolean contains(float x, float y) {
			return x > x1 && x < x2 && y > y1 && y < y2;
		}
	}

	@Override protected void end () {
		renderer.end();
	}
}
