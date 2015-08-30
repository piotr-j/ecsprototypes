package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.rendering;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import io.piotrjastrzebski.ecsclones.base.GameScreen;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.physics.Physics;

/**
 * Created by PiotrJ on 29/08/15.
 */
@Wire
public class Box2dDebugRenderer extends BaseSystem {
	@Wire(name = GameScreen.WIRE_GAME_CAM) OrthographicCamera camera;
	@Wire Physics physics;

	Box2DDebugRenderer renderer;

	@Override protected void initialize () {
		renderer = new Box2DDebugRenderer();
	}

	@Override protected void processSystem () {
		renderer.render(physics.getWorld(), camera.combined);
	}
}
