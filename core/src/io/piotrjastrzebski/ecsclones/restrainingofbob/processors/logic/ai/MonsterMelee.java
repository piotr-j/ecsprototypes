package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.Manager;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.status.HitBy;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.Finder;

/**
 * Created by PiotrJ on 31/08/15.
 */
@Wire
public class MonsterMelee extends Manager {
	private final static String TAG = MonsterMelee.class.getSimpleName();
	protected ComponentMapper<HitBy> mHitBy;
	Finder finder;

	public MonsterMelee () {
	}

	TagManager tags;
	public boolean attack (int attacker, String target) {
		Entity te = tags.getEntity(target);
		if (te == null) {
			Gdx.app.log(TAG, "Invalid tag " + target + "!");
			return false;
		}
		// TODO get range and dmg from attacker
		HitBy hit = mHitBy.create(te);
		Gdx.app.log(TAG, attacker + " attacks " + target);
		hit.dmg += 0.5f;
		return true;
	}
}
