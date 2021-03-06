package io.piotrjastrzebski.ecsclones.flapper;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import io.piotrjastrzebski.ecsclones.base.components.Collided;
import io.piotrjastrzebski.ecsclones.flapper.components.Flapper;
import io.piotrjastrzebski.ecsclones.flapper.components.FlapperKiller;
import io.piotrjastrzebski.ecsclones.flapper.components.FlapperScorer;

/**
 * Created by PiotrJ on 04/08/15.
 */
@Wire
public class FlapperCollisionResolver extends IteratingSystem {
	protected ComponentMapper<Collided> mCollided;
	protected ComponentMapper<Flapper> mFlapper;
	protected ComponentMapper<FlapperKiller> mKiller;
	protected ComponentMapper<FlapperScorer> mScore;


	public FlapperCollisionResolver () {
		super(Aspect.all(Collided.class, Flapper.class));
	}

	@Override protected void inserted (int eid) {
		// here on in process?
		Collided collided = mCollided.get(eid);
		for (int i = 0; i < collided.with.size(); i++) {
			Entity o = world.getEntity(collided.with.get(i));
			if (mKiller.has(o)) {
				// dead
				world.delete(eid);
				Gdx.app.log("", "Died! ");
			}
			if (mScore.has(o)) {
				FlapperScorer scorer = mScore.get(o);
				if (scorer.consumed) continue;
				scorer.consumed = true;
				Flapper flapper = mFlapper.get(eid);
				flapper.score++;
				// scorer is no longer required
				o.deleteFromWorld();
				Gdx.app.log("", "Scored! " + flapper.score);
			}
		}
	}

	@Override protected void process (int eid) {

	}
}
