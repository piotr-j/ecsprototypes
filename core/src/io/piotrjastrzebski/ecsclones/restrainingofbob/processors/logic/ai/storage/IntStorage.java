package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai.storage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.badlogic.gdx.utils.ObjectMap;

public class IntStorage {
	private ObjectMap<String, IntArray> keyToStack = new ObjectMap<>();

	public boolean hasStack (String name) {
		return keyToStack.containsKey(name);
	}

	public IntArray getStack (String name) {
		IntArray stack = keyToStack.get(name, null);
		if (stack == null) {
			stack = new IntArray(8);
			keyToStack.put(name, stack);
		}
		return stack;
	}

	public boolean isStackEmpty (String name) {
		IntArray stack = keyToStack.get(name, null);
		return stack == null || stack.size == 0;
	}

	public void pushToStack (String name, int value) {
		getStack(name).add(value);
	}

	public int popFromStack (String name) {
		IntArray stack = keyToStack.get(name, null);
		if (stack == null) return -1;
		return stack.size > 0? stack.pop():-1;
	}

	private ObjectIntMap<String> keyToValue = new ObjectIntMap<>();

	public boolean hasValue (String key) {
		return keyToValue.containsKey(key);
	}

	public void putValue (String key, int value) {
		keyToValue.put(key, value);
	}

	public int getValue (String key) {
		if (!hasValue(key)) {
			Gdx.app.error("", "Missing int key " + key);
			return 0;
		}
		return keyToValue.get(key, 0);
	}

	public void clear () {
		// should we pool arrays and stuff?
		keyToStack.clear();
		keyToValue.clear();
	}
}
