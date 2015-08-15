package io.piotrjastrzebski.ecsclones.slinger.components;

import com.artemis.PooledComponent;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;

/**
 * Marker for entity that is currently attached to the sling
 * Created by EvilEntity on 15/08/2015.
 */
public class Slinging extends PooledComponent {
	public DistanceJoint joint;
	public int slingID;

	@Override protected void reset () {
		joint = null;
		slingID = -1;
	}
}
