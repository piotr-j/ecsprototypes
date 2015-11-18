package io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai;

import com.artemis.PooledComponent;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai.storage.FloatStorage;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai.storage.IntStorage;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai.storage.StringStorage;

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

	protected IntStorage intStorage;
	protected FloatStorage floatStorage;
	protected StringStorage stringStorage;

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
		if (intStorage != null) intStorage.clear();
		if (floatStorage != null) floatStorage.clear();
		if (stringStorage != null) stringStorage.clear();
	}

	public IntStorage getIntStorage () {
		if (intStorage == null) intStorage = new IntStorage();
		return intStorage;
	}

	public FloatStorage getFloatStorage () {
		if (floatStorage == null) floatStorage = new FloatStorage();
		return floatStorage;
	}

	public StringStorage getStringStorage () {
		if (stringStorage == null) stringStorage = new StringStorage();
		return stringStorage;
	}
}
