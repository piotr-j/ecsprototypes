package io.piotrjastrzebski.ecsclones.jumper.processors;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.EntitySystem;

/**
 * Created by EvilEntity on 22/09/2015.
 */
public class SpineLoader extends BaseEntitySystem {
	public SpineLoader () {
		super(Aspect.all());
	}

	@Override protected void processSystem () {

	}

	@Override protected void inserted (int entityId) {
		super.inserted(entityId);
	}
}
