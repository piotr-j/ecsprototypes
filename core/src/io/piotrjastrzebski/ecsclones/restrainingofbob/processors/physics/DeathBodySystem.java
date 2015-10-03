package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.physics;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.status.Dead;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.PBody;

/**
 * Created by PiotrJ on 29/08/15.
 */
@Wire
public class DeathBodySystem extends BaseEntitySystem {
	protected ComponentMapper<PBody> mPBody;

	public DeathBodySystem () {
		super(Aspect.all(Dead.class, PBody.class));
	}

	@Override protected void processSystem () {

	}

	@Override protected void inserted (int eid) {
		Body body = mPBody.get(eid).body;
		for (Fixture fixture : body.getFixtureList()) {
			Filter filter = fixture.getFilterData();
			filter.categoryBits = Physics.CAT_DEAD;
			filter.maskBits = Physics.MASK_DEAD;
			fixture.setFilterData(filter);
		}
	}
}
