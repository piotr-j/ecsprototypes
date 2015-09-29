package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic;

import com.artemis.*;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import io.piotrjastrzebski.ecsclones.restrainingofbob.RoBScreen;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.*;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.RemoveAfter;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.PBodyDef;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.PCircle;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.PSteerable;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.rendering.DebugTint;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.physics.Physics;

/**
 * Created by PiotrJ on 31/08/15.
 */
@Wire
public class PlayerSpawner extends EntitySystem {

	public PlayerSpawner () {
		super(Aspect.all(Player.class));
	}

	@Override protected void initialize () {
		super.initialize();
		spawnPlayer();
	}

	float delay;
	@Override protected void processSystem () {
		if (delay > 0) {
			delay-=world.delta;
			if (delay <= 0) {
				spawnPlayer();
			}
		}
	}

	@Override protected void removed (int entityId) {
		delay = 2;
	}

	private void spawnPlayer () {
		Entity player = world.createEntity();
		world.getManager(TagManager.class).register("player", player);
		EntityEdit edit = player.edit();
		edit.create(Player.class).name = "Player 1";
		Transform transform = edit.create(Transform.class);
		transform.pos.set(
			MathUtils.random(-RoBScreen.VP_WIDTH/2, RoBScreen.VP_WIDTH/2),
			MathUtils.random(-RoBScreen.VP_HEIGHT/2, RoBScreen.VP_HEIGHT/2)
		);


		edit.create(DebugTint.class).color.set(Color.CYAN);
		edit.create(CircleBounds.class).radius(.5f);

		Shooter shooter = edit.create(Shooter.class);
		// how often can it shoot
		shooter.delay = 0.1f;
		// dmg of the projectile
		shooter.dmg = 1;
		// +- fraction of vel
		shooter.dmgSpread = .5f;
		// +- fraction of vel
//		shooter.dmgSpreadMult = 0.1f;
		shooter.vel = 10;
		// +- fraction of vel
		shooter.velSpread = 0.1f;
		// alive time in seconts for projectile
		shooter.alive = 1f;
		// +- fraction of alive
		shooter.aliveSpread = 0.1f;
		// fraction of src vel to add to projectile
		shooter.srcVelMult = 0.5f;

		shooter.collisionCategory = Physics.CAT_PROJECTILE_P;
		shooter.collisionMask = Physics.CAT_ENEMY;

		edit.create(Health.class).hp(10);

		Mover mover = edit.create(Mover.class);
		mover.maxLinearImp = 1f;

		PBodyDef bodyDef = edit.create(PBodyDef.class);
		bodyDef.type(BodyDef.BodyType.DynamicBody);
		bodyDef.def.linearDamping = mover.maxLinearImp * 10f;
		bodyDef.def.fixedRotation = true;
		bodyDef.restitution = .25f;
		bodyDef.friction = .5f;
		bodyDef.density = 1;

		bodyDef.categoryBits = Physics.CAT_PLAYER;
		bodyDef.maskBits = Physics.MASK_PLAYER;

		PCircle pCircle = edit.create(PCircle.class);
		pCircle.setSize(1f);

		CamFollow follow = edit.create(CamFollow.class);
		follow.offsetX = 0.5f;
		follow.offsetY = 0.5f;

		PSteerable physSteerable = edit.create(PSteerable.class);
		physSteerable.setMaxLinearAcceleration(4);
		physSteerable.setMaxLinearSpeed(1);
		physSteerable.setMaxAngularAcceleration(0.5f); // greater than 0 because independent facing is enabled
		physSteerable.setMaxAngularSpeed(5);
		physSteerable.setIndependentFacing(true);
		physSteerable.setBoundingRadius(0.25f);

		edit.create(MoveFacing.class);
		edit.create(AimDirection.class);
		edit.create(Velocity.class);

		edit.create(Invulnerable.class);
		edit.create(RemoveAfter.class).setDelay(3).add(Invulnerable.class);
	}
}
