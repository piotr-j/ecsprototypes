package io.piotrjastrzebski.ecsclones.slinger.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.piotrjastrzebski.ecsclones.slinger.SlingerScreen;
import io.piotrjastrzebski.ecsclones.slinger.components.*;

/**
 * Created by EvilEntity on 15/08/2015.
 */
@Wire
public class DebugRenderer extends EntityProcessingSystem {
	private ComponentMapper<Transform> mTransform;
	private ComponentMapper<Size> mSize;
	private ComponentMapper<Radius> mRadius;
	private ComponentMapper<Tint> mTint;

	@Wire(name = SlingerScreen.WIRE_GAME_CAM) OrthographicCamera camera;
	@Wire ShapeRenderer renderer;

	public DebugRenderer () {
		super(Aspect.all(ShapeRenderable.class, Transform.class).one(Size.class, Radius.class));
	}

	@Override protected void begin () {
		renderer.setProjectionMatrix(camera.combined);
		renderer.setColor(Color.WHITE);
		renderer.begin(ShapeRenderer.ShapeType.Line);
	}

	@Override protected void process (Entity e) {
		Tint tint = mTint.get(e);
		if (tint != null) {
			renderer.setColor(tint.color);
		}
		Transform tf = mTransform.get(e);
		Size size = mSize.get(e);
		if (size != null) {
			// TODO rotation
			renderer.rect(tf.x, tf.y, size.width, size.height);
		}

		Radius radius = mRadius.get(e);
		if (radius != null) {
			renderer.circle(tf.x + radius.radius, tf.y + radius.radius, radius.radius, 16);
		}
		if (tint != null) {
			renderer.setColor(Color.WHITE);
		}
	}

	@Override protected void end () {
		renderer.end();
	}
}
