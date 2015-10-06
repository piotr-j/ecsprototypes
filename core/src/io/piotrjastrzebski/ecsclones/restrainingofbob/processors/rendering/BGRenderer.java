package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.rendering;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import io.piotrjastrzebski.ecsclones.base.GameScreen;

/**
 * Created by PiotrJ on 29/08/15.
 */
@Wire
public class BGRenderer extends BaseSystem {
	@Wire(name = GameScreen.WIRE_GAME_CAM) OrthographicCamera camera;
	@Wire(name = GameScreen.WIRE_GAME_VP) ExtendViewport viewport;
	@Wire ShapeRenderer renderer;


	public BGRenderer () {
		super();
	}

	@Override protected void processSystem () {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		renderer.setProjectionMatrix(camera.combined);
		renderer.setColor(1, 1, 1, 0.1f);
		renderer.begin(ShapeRenderer.ShapeType.Filled);

		float width = viewport.getWorldWidth() + 4;
		float height = viewport.getWorldHeight() + 4;
		float x = (int)(camera.position.x - width /2 - 2);
		x -= x%2;
		float y = (int)(camera.position.y - height /2 - 2);
		y -= y%2;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (i % 2 == j % 2) continue;
				renderer.rect(x + i, y + j, 1, 1);
			}
		}
		renderer.end();
	}
}
