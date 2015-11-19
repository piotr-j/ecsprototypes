package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai.storage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.ObjectMap;

public class StringStorage {
	private ObjectMap<String, Array<String>> keyToStack = new ObjectMap<>();

	public boolean hasStack (String name) {
		return keyToStack.containsKey(name);
	}

	public Array<String> getStack (String name) {
		Array<String> stack = keyToStack.get(name, null);
		if (stack == null) {
			stack = new Array<>(8);
			keyToStack.put(name, stack);
		}
		return stack;
	}

	public boolean isStackEmpty (String name) {
		Array<String> stack = keyToStack.get(name, null);
		return stack == null || stack.size == 0;
	}

	public void pushToStack (String name, String value) {
		getStack(name).add(value);
	}

	public String popFromStack (String name) {
		Array<String> stack = keyToStack.get(name, null);
		if (stack == null) return "";
		return stack.size > 0? stack.pop():"";
	}

	private ObjectMap<String, String> keyToValue = new ObjectMap<>();
	public void putValue (String key, String value) {
		keyToValue.put(key, value);
	}

	public boolean hasValue (String key) {
		return keyToValue.containsKey(key);
	}

	public String getValue (String key) {
		if (!hasValue(key)) {
			Gdx.app.error("", "Missing String key " + key);
			return "";
		}
		return keyToValue.get(key);
	}

	public boolean clearValue (String varName) {
		if (!hasValue(varName)) return false;
		keyToValue.remove(varName);
		return true;
	}

	public boolean clearStack (String stack) {
		if (!hasStack(stack)) return false;
		keyToStack.get(stack, null).clear();
		return true;
	}

	public boolean popAndSet (String stack, String var) {
		Array<String> values = getStack(stack);
		if (values == null || values.size == 0) return false;
		putValue(var, values.pop());
		return false;
	}

	public boolean getAndPush (String var, String stack) {
		if (!hasValue(var)) return false;
		String value = getValue(var);
		getStack(stack).add(value);
		return true;
	}

	public void clear () {
		// should we pool arrays and stuff?
		keyToStack.clear();
		keyToValue.clear();
	}
}
