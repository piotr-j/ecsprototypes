package io.piotrjastrzebski.ecsclones.slinger.systems;

import com.artemis.*;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import io.piotrjastrzebski.ecsclones.slinger.SlingerScreen;
import io.piotrjastrzebski.ecsclones.slinger.components.*;
import io.piotrjastrzebski.ecsclones.slinger.components.Transform;
import io.piotrjastrzebski.ecsclones.slinger.components.parts.BlockDef;
import io.piotrjastrzebski.ecsclones.slinger.components.parts.GroundDef;
import io.piotrjastrzebski.ecsclones.slinger.components.parts.ProjectileDef;
import io.piotrjastrzebski.ecsclones.slinger.components.parts.SlingDef;
import io.piotrjastrzebski.ecsclones.slinger.systems.physics.Physics;

/**
 * Created by EvilEntity on 15/08/2015.
 */
@Wire
public class LevelLoader extends Manager {
	private ComponentMapper<Level> mLevel;
	@Wire Physics physics;

	public LevelLoader () {

	}

	Aspect levels;
	@Override protected void initialize () {
		super.initialize();
		levels = Aspect.all(Level.class).build(world);
	}

	@Override public void added (Entity e) {
		if (levels.isInterested(e)) {
			load(physics.getWorld(), mLevel.get(e));
		}
	}

	public void load (World world, Level level) {
		// TODO load data from level data

		createGround();
		float ground = -SlingerScreen.VP_HEIGHT / 2 + 1f;
		// need some decent way of calculating rotation position, or gui!
		createBox(-1f + 1, ground, 1f, 3f, 0);
		createBox(1f + 1, ground, 1f, 3f, 0);
		createBox(+ 1, ground + 2f, 1f, 3f, 90);
		createBox(0f + 1, ground + 4, 1f, 3f, 0);

		createBox(-1f + 5, ground, 1f, 3f, 0);
		createBox(1f + 5, ground, 1f, 3f, 0);
		createBox(+ 5, ground + 2f, 1f, 3f, 90);
		createBox(-1f + 5, ground + 4, 1f, 3f, 0);
		createBox(1f + 5, ground + 4, 1f, 3f, 0);
		createBox(+ 5, ground+2f + 4, 1f, 3f, 90);
		createBox(0f + 5, ground + 8, 1f, 3f, 0);

		createBox(-1f + 9, ground, 1f, 3f, 0);
		createBox(1f + 9, ground, 1f, 3f, 0);
		createBox(+ 9, ground + 2f, 1f, 3f, 90);
		createBox(0f + 9, ground + 4, 1f, 3f, 0);

		createSlingShot(-16, ground);
		createProjectile(-16, ground + 5, true);
	}

	private void createSlingShot (float x, float y) {
		EntityEdit edit = world.createEntity().edit();

		SlingDef slingDef = edit.create(SlingDef.class);
		slingDef.friction = 0.3f;
		slingDef.restitution = 0.2f;
		slingDef.length = 0.1f;
		slingDef.dampingRatio = 0.8f;
		slingDef.frequencyHz = 3;
		slingDef.anchor.set(0, 4);

		Transform transform = edit.create(Transform.class);
		transform.x = x;
		transform.y = y;

		Size size = edit.create(Size.class);
		size.width = 1;
		size.height = 4;
	}

	private void createProjectile (float x, float y, boolean attach) {
		EntityEdit edit = world.createEntity().edit();

		ProjectileDef projectileDef = edit.create(ProjectileDef.class);
		projectileDef.friction = 0.3f;
		projectileDef.restitution = 0.2f;
		projectileDef.density = 1;

		Transform transform = edit.create(Transform.class);
		transform.x = x;
		transform.y = y;

		Radius radius = edit.create(Radius.class);
		radius.set(0.5f);
		if (attach) {
			edit.create(AttachToSling.class);
		}
	}

	private void createGround () {
		EntityEdit edit = world.createEntity().edit();

		GroundDef groundDef = edit.create(GroundDef.class);
		groundDef.friction = 0.3f;
		groundDef.restitution = 0.2f;

		Transform transform = edit.create(Transform.class);
		transform.x = -SlingerScreen.VP_WIDTH / 2;
		transform.y = -SlingerScreen.VP_HEIGHT / 2;

		Size size = edit.create(Size.class);
		size.width = SlingerScreen.VP_WIDTH;
		size.height = 1;
	}

	private void createBox (float x, float y, float width, float height, float angle) {
		EntityEdit edit = world.createEntity().edit();

		// TODO type like wood or glass
		BlockDef blockDef = edit.create(BlockDef.class);
		blockDef.restitution = 0.2f;
		blockDef.friction = 0.3f;
		blockDef.density = 1;

		Transform transform = edit.create(Transform.class);
		transform.x = x;
		transform.y = y;
		transform.rotation = angle;

		Size size = edit.create(Size.class);
		size.width = width;
		size.height = height;
	}
}
