package io.piotrjastrzebski.ecsclones.slinger.systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import io.piotrjastrzebski.ecsclones.base.components.Position;
import io.piotrjastrzebski.ecsclones.slinger.SlingerScreen;
import io.piotrjastrzebski.ecsclones.slinger.components.Radius;
import io.piotrjastrzebski.ecsclones.slinger.components.Size;
import io.piotrjastrzebski.ecsclones.slinger.systems.physics.Physics;

/**
 * Created by EvilEntity on 15/08/2015.
 */
@Wire
public class Box2dRenderer extends BaseSystem {
	private ComponentMapper<Position> mPosition;
	private ComponentMapper<Size> mSize;
	private ComponentMapper<Radius> mRadius;

	@Wire(name = SlingerScreen.WIRE_GAME_CAM) OrthographicCamera camera;
	@Wire Physics physics;

	private Box2DDebugRenderer renderer;

	public Box2dRenderer () {
		renderer = new Box2DDebugRenderer();
	}

	@Override protected void processSystem () {
		World world = physics.getWorld();
		if (world == null) return;
		renderer.render(world, camera.combined);
	}
}
