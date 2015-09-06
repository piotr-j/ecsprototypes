package io.piotrjastrzebski.ecsclones.restrainingofbob.processors;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.Dead;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.PBody;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.physics.Physics;

/**
 * Created by PiotrJ on 29/08/15.
 */
@Wire
public class DeathBodySystem extends EntityProcessingSystem {
	protected ComponentMapper<PBody> mPBody;

	public DeathBodySystem () {
		super(Aspect.all(Dead.class, PBody.class));
	}

	@Override protected void inserted (Entity e) {
		Body body = mPBody.get(e).body;
		for (Fixture fixture : body.getFixtureList()) {
			Filter filter = fixture.getFilterData();
			filter.categoryBits = Physics.CAT_DEAD;
			filter.maskBits = Physics.MASK_DEAD;
			fixture.setFilterData(filter);
		}
	}

	@Override protected void process (Entity e) {

	}
}
