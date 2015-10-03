package io.piotrjastrzebski.ecsclones.slinger.systems.physics;

import com.artemis.*;
import com.artemis.annotations.Wire;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import io.piotrjastrzebski.ecsclones.slinger.components.AttachToSling;
import io.piotrjastrzebski.ecsclones.slinger.components.Slinging;
import io.piotrjastrzebski.ecsclones.slinger.components.parts.Projectile;
import io.piotrjastrzebski.ecsclones.slinger.components.parts.Sling;

/**
 * Created by EvilEntity on 15/08/2015.
 */
@Wire
public class ProjectileLinker extends BaseEntitySystem {
	private ComponentMapper<Projectile> mProjectile;
	private ComponentMapper<Slinging> mSlinging;
	private ComponentMapper<Sling> mSling;

	@Wire Physics physics;
	@Wire SlingMaker slingMaker;

	public ProjectileLinker () {
		super(Aspect.all(Projectile.class, AttachToSling.class));
	}

	EntitySubscription slings;
	@Override protected void initialize () {
		super.initialize();
		slings = world.getSystem(AspectSubscriptionManager.class).get(Aspect.all(Sling.class));
	}

	@Override protected void inserted (int eid) {
		Projectile projectile = mProjectile.get(eid);
		IntBag entities = slings.getEntities();
		// TODO solve this in cleaner way
		Sling sling = mSling.get(slingMaker.latestID());

		// attach to first sling as there should be only one at any rate...
//		Sling sling = mSling.get(entities.get(0));

//		Body projectile = createProjectile(world, x, y + 3);
		DistanceJointDef jointDef = new DistanceJointDef();
		jointDef.bodyA = sling.body;
		jointDef.bodyB = projectile.body;
		jointDef.collideConnected = false;
		jointDef.length = sling.length;
		jointDef.dampingRatio = sling.dampingRatio;
		jointDef.frequencyHz = sling.frequencyHz;
		jointDef.localAnchorA.set(sling.anchor);
		jointDef.localAnchorB.set(0, 0);

		Slinging slinging = mSlinging.create(eid);
		slinging.joint = (DistanceJoint)physics.getB2DWorld().createJoint(jointDef);
		slinging.slingID = slingMaker.latestID();
	}

	@Override protected void processSystem () {

	}
}
