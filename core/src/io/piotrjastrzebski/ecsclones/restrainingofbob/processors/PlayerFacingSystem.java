package io.piotrjastrzebski.ecsclones.restrainingofbob.processors;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.Facing;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.Player;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.Transform;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.PBody;

/**
 * Created by PiotrJ on 29/08/15.
 */
@Wire
public class PlayerFacingSystem extends EntityProcessingSystem {
	protected ComponentMapper<Facing> mFacing;
	protected ComponentMapper<PBody> mPBody;

	public PlayerFacingSystem () {
		super(Aspect.all(Player.class, Facing.class, PBody.class));
	}

	@Override protected void process (Entity e) {
		Facing f = mFacing.get(e);
		PBody pBody = mPBody.get(e);
		Vector2 pos = pBody.body.getPosition();
		pBody.body.setTransform(pos.x, pos.y, f.dir.angle * MathUtils.degreesToRadians);
	}
}
