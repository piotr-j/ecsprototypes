package io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.status;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by PiotrJ on 26/08/15.
 */
public class Health extends PooledComponent {
	public float maxHp;
	public float hp;

	@Override protected void reset () {
		maxHp = hp = 0;
	}

	public void hp (float hp) {
		this.hp = maxHp = hp;
	}

	public void addHP (float value) {
		if (hp <= 0) return;
		hp = MathUtils.clamp(hp + value, 0, maxHp);
	}
}
