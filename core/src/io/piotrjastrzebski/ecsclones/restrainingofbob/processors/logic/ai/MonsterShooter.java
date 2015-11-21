package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.Manager;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.AimDirection;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.Shooter;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.tags.Shoot;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.Transform;

/**
 * Created by PiotrJ on 31/08/15.
 */
@Wire
public class MonsterShooter extends Manager implements MonsterAttack.AttackExecutor {
	private final static String TAG = MonsterShooter.class.getSimpleName();
	protected ComponentMapper<AimDirection> mAimDirection;
	protected ComponentMapper<Transform> mTransform;
	protected ComponentMapper<Shoot> mShoot;
	protected ComponentMapper<Shooter> mShooter;

	public MonsterShooter () {}

	@Override protected void initialize () {
		super.initialize();
		world.getSystem(MonsterAttack.class).register(this);
	}

	TagManager tags;
	Vector2 tmp = new Vector2();

	@Override public boolean accept (int entityId) {
		return mShooter.has(entityId);
	}

	@Override public boolean attack (int attacker, String target) {
		Entity te = tags.getEntity(target);
		if (te == null) {
			Gdx.app.log(TAG, "Invalid tag " + target + "!");
			return false;
		}

		AimDirection aim = mAimDirection.get(attacker);
		Vector2 ap = mTransform.get(attacker).pos;
		Vector2 tp = mTransform.get(te).pos;
		aim.angle = tmp.set(tp).sub(ap).angle();;
		// at what point do we shoot exactly? melee is in here
		mShoot.create(attacker);
		return true;
	}
}
