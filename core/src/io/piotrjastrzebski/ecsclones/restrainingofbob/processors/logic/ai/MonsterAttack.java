package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai;

import com.artemis.Manager;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.utils.Array;

/**
 * Created by PiotrJ on 31/08/15.
 */
@Wire
public class MonsterAttack extends Manager {
	private final static String TAG = MonsterAttack.class.getSimpleName();

	public MonsterAttack () {}

	public Array<AttackExecutor> executors = new Array<>();

	public boolean attack (int attacker, String target) {
		// can attack be multiple of those at the same time? probably not
		for (AttackExecutor executor : executors) {
			if (executor.accept(attacker)) {
				return executor.attack(attacker, target);
			}
		}
		return false;
	}

	public void register (AttackExecutor executor) {
		if (!executors.contains(executor, true)) {
			executors.add(executor);
		}
	}

	public interface AttackExecutor {
		boolean accept(int entityId);
		boolean attack(int entityId, String target);
	}
}
