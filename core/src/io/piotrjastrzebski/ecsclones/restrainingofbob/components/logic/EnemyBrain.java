package io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic;

import com.artemis.PooledComponent;

/**
 * Created by PiotrJ on 31/08/15.
 */
public class EnemyBrain extends PooledComponent {
	// current hp of entity
	public float hp;
	public float maxHP;
	// min dst2 to pursue target
	public float minDst2;
//	@EntityID
	public int target;
	//	@EntityID
	public int id;
	public String treePath;
	public boolean inRange;

	public EnemyBrain () {
		reset();
	}

	@Override protected void reset () {
		hp = 0;
		maxHP = 0;
		minDst2 = 0;
		target = -1;
		id = -1;
		inRange = false;
	}
}
