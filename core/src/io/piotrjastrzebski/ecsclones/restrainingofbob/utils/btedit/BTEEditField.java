package io.piotrjastrzebski.ecsclones.restrainingofbob.utils.btedit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.kotcrab.vis.ui.FocusManager;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisValidableTextField;

/**
 * Created by PiotrJ on 06/10/15.
 */
public class BTEEditField {
	private final static String TAG = BTEEditField.class.getSimpleName();

	protected static Actor createEditField (final Object object, final Field field) throws ReflectionException {
		Class fType = field.getType();
		if (fType == float.class) {
			return BTEEditField.floatEditField(object, field);
		} else if (fType == int.class) {
			return BTEEditField.integerEditField(object, field);
		} else if (fType == String.class) {
			return BTEEditField.stringEditField(object, field);
		} else if (fType == boolean.class) {
			return BTEEditField.booleanEditField(object, field);
		} else if (fType.isEnum()) {
			return BTEEditField.enumEditField(object, field);
		} else {
			Gdx.app.error(TAG, "Not supported field type " + fType + " in " + object);
			return null;
		}
	}

	protected static Actor integerEditField (final Object object, final Field field) throws ReflectionException {
		int value = (int)field.get(object);
		final VisValidableTextField vtf = new VisValidableTextField(true, Validators.INTEGERS);
		vtf.setText(Integer.toString(value));
		vtf.setTextFieldListener(new VisTextField.TextFieldListener() {
			@Override public void keyTyped (VisTextField textField, char c) {
				vtf.validateInput();
				if (vtf.isInputValid()) {
					int value;
					try {
						value = Integer.parseInt(vtf.getText());
					} catch (NumberFormatException e) {
						return;
					}

					try {
						field.set(object, value);
					} catch (ReflectionException e) {
						Gdx.app.error("Integer validator", "Failed to set field " + field + " to " + vtf.getText(), e);
					}
				}
			}
		});
		addCancelOnESC(vtf);
		return vtf;
	}

	protected static Actor floatEditField (final Object object, final Field field) throws ReflectionException {
		float value = (float)field.get(object);
		final VisValidableTextField vtf = new VisValidableTextField(true, Validators.FLOATS);
		vtf.setText(Float.toString(value));
		vtf.setTextFieldListener(new VisTextField.TextFieldListener() {
			@Override public void keyTyped (VisTextField textField, char c) {
				vtf.validateInput();
				if (vtf.isInputValid()) {
					float value;
					try {
						value = Float.parseFloat(vtf.getText());
					} catch (NumberFormatException e) {
						return;
					}
					try {
						field.set(object, value);
					} catch (ReflectionException e) {
						Gdx.app.error("Float validator", "Failed to set field " + field + " to " + vtf.getText(), e);
					}
				}
			}
		});
		addCancelOnESC(vtf);
		return vtf;
	}

	protected static Actor stringEditField (final Object object, final Field field) throws ReflectionException {
		String value = (String)field.get(object);
		final VisTextField tf = new VisTextField(value);
		tf.addListener(new ChangeListener() {
			@Override public void changed (ChangeEvent event, Actor actor) {
				try {
					field.set(object, tf.getText());
				} catch (ReflectionException e) {
					Gdx.app.error("String validator", "Failed to set field " + field + " to " + tf.getText(), e);
				}
			}
		});
		addCancelOnESC(tf);
		return tf;
	}

	protected static Actor enumEditField (final Object object, final Field field) throws ReflectionException {
		Object[] values = field.getType().getEnumConstants();
		final VisSelectBox<Object> sb = new VisSelectBox<>();
		sb.setItems(values);
		sb.setSelected(field.get(object));
		sb.addListener(new ChangeListener() {
			@Override public void changed (ChangeEvent event, Actor actor) {
				Object selected = sb.getSelection().getLastSelected();
				try {
					field.set(object, selected);
				} catch (ReflectionException e) {
					Gdx.app.error("Enum validator", "Failed to set field " + field + " to " + selected, e);
				}
			}
		});
		return sb;
	}

	protected static Actor booleanEditField (final Object object, final Field field) throws ReflectionException {
		final VisSelectBox<Object> sb = new VisSelectBox<>();
		sb.setItems(true, false);
		sb.setSelected(field.get(object));
		sb.addListener(new ChangeListener() {
			@Override public void changed (ChangeEvent event, Actor actor) {
				Object selected = sb.getSelection().getLastSelected();
				try {
					field.set(object, selected);
				} catch (ReflectionException e) {
					Gdx.app.error("Boolean validator", "Failed to set field " + field + " to " + selected, e);
				}
			}
		});
		return sb;
	}

	private static void addCancelOnESC (final Actor actor) {
		actor.addListener(new InputListener() {
			@Override public boolean keyDown (InputEvent event, int keycode) {
				if (keycode == Input.Keys.ESCAPE) {
					FocusManager.getFocus();
					actor.getStage().setKeyboardFocus(null);
				}
				return false;
			}
		});
	}
}
