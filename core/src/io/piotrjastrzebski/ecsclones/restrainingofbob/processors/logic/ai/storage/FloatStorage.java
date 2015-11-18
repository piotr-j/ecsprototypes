package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai.storage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.badlogic.gdx.utils.ObjectMap;

public class FloatStorage {
	private ObjectMap<String, FloatArray> keyToStack = new ObjectMap<>();

	public boolean hasStack (String name) {
		return keyToStack.containsKey(name);
	}

	public FloatArray getStack (String name) {
		FloatArray stack = keyToStack.get(name, null);
		if (stack == null) {
			stack = new FloatArray(8);
			keyToStack.put(name, stack);
		}
		return stack;
	}

	public boolean isStackEmpty (String name) {
		FloatArray stack = keyToStack.get(name, null);
		return stack == null || stack.size == 0;
	}

	public void pushToStack (String name, float value) {
		getStack(name).add(value);
	}

	public float popFromStack (String name) {
		FloatArray stack = keyToStack.get(name, null);
		if (stack == null) return -1;
		return stack.size > 0? stack.pop():-1;
	}

	private ObjectIntMap<String> keyToValue = new ObjectIntMap<>();
	public boolean hasValue (String key) {
		return keyToValue.containsKey(key);
	}

	public void putValue (String key, float value) {
		keyToValue.put(key, Float.floatToIntBits(value));
	}

	public float getValue (String key) {
		if (!hasValue(key)) {
			Gdx.app.error("", "Missing float key " + key);
			return 0;
		}
		return Float.intBitsToFloat(keyToValue.get(key, 0));
	}

	public void clear () {
		// should we pool arrays and stuff?
		keyToStack.clear();
		keyToValue.clear();
	}
}
