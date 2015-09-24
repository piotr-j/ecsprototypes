package io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic;

import com.artemis.PooledComponent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.badlogic.gdx.utils.ObjectMap;

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
		keyToString.clear();
		keyToFloat.clear();
		keyToInt.clear();
	}

	private ObjectIntMap<String> keyToFloat = new ObjectIntMap<>();
	public void put (String key, float value) {
		keyToFloat.put(key, Float.floatToIntBits(value));
	}

	public float getFloat (String key) {
		if (!keyToFloat.containsKey(key)) {
			Gdx.app.error("", "Missing float key " + key);
			return 0;
		}
		return Float.intBitsToFloat(keyToFloat.get(key, 0));
	}

	private ObjectIntMap<String> keyToInt = new ObjectIntMap<>();
	public void put (String key, int value) {
		keyToInt.put(key, value);
	}

	public int getInt (String key) {
		if (!keyToInt.containsKey(key)) {
			Gdx.app.error("", "Missing int key " + key);
			return 0;
		}
		return keyToInt.get(key, 0);
	}

	private ObjectMap<String, String> keyToString = new ObjectMap<>();
	public void put (String key, String value) {
		keyToString.put(key, value);
	}

	public String getString (String key) {
		if (!keyToString.containsKey(key)) {
			Gdx.app.error("", "Missing String key " + key);
			return "";
		}
		return keyToString.get(key);
	}
}
