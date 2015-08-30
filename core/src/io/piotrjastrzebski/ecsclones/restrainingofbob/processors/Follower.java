package io.piotrjastrzebski.ecsclones.restrainingofbob.processors;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.piotrjastrzebski.ecsclones.base.GameScreen;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.CamFollow;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.CircleBounds;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.RectBounds;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.Transform;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.rendering.DebugTint;

/**
 * Created by PiotrJ on 29/08/15.
 */
@Wire
public class Follower extends EntityProcessingSystem {
	@Wire(name = GameScreen.WIRE_GAME_CAM) OrthographicCamera camera;

	protected ComponentMapper<Transform> mTransform;
	protected ComponentMapper<CamFollow> mCamFollow;

	public Follower () {
		super(Aspect.all(Transform.class, CamFollow.class));
	}


	@Override protected void process (Entity e) {
		Transform trans = mTransform.get(e);
		CamFollow follow = mCamFollow.get(e);

		camera.position.set(trans.pos.x + follow.offsetX, trans.pos.y + follow.offsetY, 0);
		camera.update();
	}

}
