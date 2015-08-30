package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.physics;

import com.artemis.Manager;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * Dispatches stuff
 * Created by PiotrJ on 22/08/15.
 */
@Wire public class PhysicsContacts extends Manager implements ContactListener {
	// TODO do we want this to be a manager? doesnt really need anything...
	@Override public void beginContact (Contact contact) {
		Physics.UserData userDataA = (Physics.UserData)contact.getFixtureA().getBody().getUserData();
		Physics.UserData userDataB = (Physics.UserData)contact.getFixtureB().getBody().getUserData();

		//TODO events? yay/nay? if deferred till the end of frame, would allow for direct box2d stuff modification
		// TODO could make some component... but need to handle multiple contacts per frame
		if (userDataA != null) userDataA.onContact(userDataB);
		if (userDataB != null) userDataB.onContact(userDataA);
	}

	@Override public void endContact (Contact contact) {
		Physics.UserData userDataA = (Physics.UserData)contact.getFixtureA().getBody().getUserData();
		Physics.UserData userDataB = (Physics.UserData)contact.getFixtureB().getBody().getUserData();

		if (userDataA != null) userDataA.onEndContact(userDataB);
		if (userDataB != null) userDataB.onEndContact(userDataA);
	}

	@Override public void preSolve (Contact contact, Manifold oldManifold) {
		// TODO do we want something in here?
	}

	@Override public void postSolve (Contact contact, ContactImpulse impulse) {
		// more often then not its 1 or 2. count is number of contact points
		float[] normalImpulses = impulse.getNormalImpulses();
		// TODO what do we want with this? average? max? first only?
		// http://www.iforce2d.net/b2dtut/sticky-projectiles says first value
		float strength = normalImpulses[0];

		Physics.UserData userDataA = (Physics.UserData)contact.getFixtureA().getBody().getUserData();
		Physics.UserData userDataB = (Physics.UserData)contact.getFixtureB().getBody().getUserData();

		if (userDataA != null) userDataA.onPostSolve(userDataB, strength);
		if (userDataB != null) userDataB.onPostSolve(userDataA, strength);
	}
}
