package io.piotrjastrzebski.ecsclones.flapper;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityEdit;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import io.piotrjastrzebski.ecsclones.base.components.Collider;
import io.piotrjastrzebski.ecsclones.base.components.DebugRender;
import io.piotrjastrzebski.ecsclones.base.components.Position;
import io.piotrjastrzebski.ecsclones.base.components.Size;
import io.piotrjastrzebski.ecsclones.flapper.components.Flapper;
import io.piotrjastrzebski.ecsclones.flapper.components.FlapperKiller;
import io.piotrjastrzebski.ecsclones.flapper.components.FlapperScorer;

/**
 * Created by PiotrJ on 08/08/15.
 */
@Wire
public class WorldGenerator extends EntityProcessingSystem {
	@Wire
	OrthographicCamera camera;
	@Wire ExtendViewport viewport;

	protected ComponentMapper<Position> mPosition;
	protected ComponentMapper<Size> mSize;

	public WorldGenerator () {
		super(Aspect.all(Flapper.class, Position.class, Size.class));
	}

	public static final float MIN_OBST_DST = 6;
	public static final float MAX_OBST_DST = 12;
	float lastX;
	@Override protected void inserted (Entity e) {
		Position position = mPosition.get(e);
		lastX = position.pos.x;
		createSky(lastX -FlapperScreen.VP_WIDTH / 2, FlapperScreen.VP_WIDTH);
		createGround(lastX -FlapperScreen.VP_WIDTH / 2, FlapperScreen.VP_WIDTH);
	}

	@Override protected void process (Entity e) {
		// generate world as needed
		Position position = mPosition.get(e);
		Size size = mSize.get(e);

		// spawn every x distance traveled?
		if (lastX <= position.pos.x) {
			// how far from current position to spawn
			float newDst = MathUtils.random(MIN_OBST_DST, MAX_OBST_DST);

			createSky(lastX, newDst);
			createGround(lastX, newDst);
			// - width of obs
			createObstacle(lastX + newDst - 1);
			lastX+=newDst;
		}
	}

	private void createGround (float x, float width) {
		Entity entity = world.createEntity();
		EntityEdit edit = entity.edit();

		Position position = edit.create(Position.class);
		position.pos.set(x, -FlapperScreen.VP_HEIGHT / 2);

		Size size = edit.create(Size.class);
		size.width = width;
		size.height = 1;

		DebugRender debugRender = edit.create(DebugRender.class);
		debugRender.color.set(Color.OLIVE);

		edit.create(FlapperKiller.class);
		edit.create(Collider.class);

	}

	private void createSky (float x, float width) {
		Entity entity = world.createEntity();
		EntityEdit edit = entity.edit();

		Position position = edit.create(Position.class);
		position.pos.set(x, FlapperScreen.VP_HEIGHT / 2 - 1);

		Size size = edit.create(Size.class);
		size.width = width;
		size.height = 1;

		DebugRender debugRender = edit.create(DebugRender.class);
		debugRender.color.set(Color.CYAN);

		edit.create(FlapperKiller.class);
		edit.create(Collider.class);
	}

	final static float GAP = 4;
	final static float MARGIN = 3;
	private void createObstacle (float x) {
		// create top, bottom and sensor
		float ty = MathUtils.random(-FlapperScreen.VP_HEIGHT / 2 + 1 + MARGIN, FlapperScreen.VP_HEIGHT / 2 - 1 - GAP - MARGIN);
		float y = -FlapperScreen.VP_HEIGHT / 2 + 1;
		createObstacle(x, y, ty - y);
		createScore(x, ty, GAP);
		createObstacle(x, ty + GAP, FlapperScreen.VP_HEIGHT / 2 - 1 - (ty + GAP));
	}

	private void createObstacle (float x, float y, float height) {
		Entity entity = world.createEntity();
		EntityEdit edit = entity.edit();

		Position position = edit.create(Position.class);
		position.pos.set(x, y);

		Size size = edit.create(Size.class);
		size.width = 1;
		size.height = height;

		DebugRender debugRender = edit.create(DebugRender.class);
		debugRender.color.set(Color.GREEN);

		edit.create(FlapperKiller.class);
		edit.create(Collider.class);
	}

	private void createScore (float x, float y, float height) {
		Entity entity = world.createEntity();
		EntityEdit edit = entity.edit();

		Position position = edit.create(Position.class);
		position.pos.set(x, y);

		Size size = edit.create(Size.class);
		size.width = 1;
		size.height = height;

		DebugRender debugRender = edit.create(DebugRender.class);
		debugRender.color.set(Color.PINK);

		edit.create(FlapperScorer.class);
		edit.create(Collider.class);
	}

}
