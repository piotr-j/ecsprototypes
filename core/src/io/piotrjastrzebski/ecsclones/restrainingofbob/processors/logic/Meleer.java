package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.Manager;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.HitBy;

/**
 * Created by PiotrJ on 31/08/15.
 */
@Wire
public class Meleer extends Manager {
	private final static String TAG = Meleer.class.getSimpleName();
	protected ComponentMapper<HitBy> mHitBy;
	Finder finder;
	public Meleer () {
	}

	TagManager tags;
	public void attack (int attacker, String target) {
		Gdx.app.log(TAG, attacker + " attacks " + target);
		Entity te = tags.getEntity(target);
		if (te == null) {
			Gdx.app.log(TAG, "Invalid tag " + target + "!");
			return;
		}
		HitBy hit = mHitBy.getSafe(te);
		if (hit == null) {
			hit = te.edit().create(HitBy.class);
		}
		hit.dmg += 0.5f;
	}
}
