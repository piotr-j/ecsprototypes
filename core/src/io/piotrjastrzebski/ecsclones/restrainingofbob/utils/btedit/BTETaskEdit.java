package io.piotrjastrzebski.ecsclones.restrainingofbob.utils.btedit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.reflect.Annotation;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

/**
 * Created by PiotrJ on 07/10/15.
 */
class BTETaskEdit extends VisTable implements Pool.Poolable {
	private final static String TAG = BTETaskEdit.class.getSimpleName();
	private VisLabel taskName;

	public BTETaskEdit () {
		super(true);
		taskName = new VisLabel();
	}

	public void init (Task task) {
		reset();

		if (task == null)
			return;
		taskName.setText(task.getClass().getSimpleName());
		add(taskName).colspan(2);
		row();
		addEditFields(task);
		pack();
	}

	private void addEditFields (Task task) {
		Class<?> aClass = task.getClass();
		Field[] fields = ClassReflection.getFields(aClass);
		for (Field f : fields) {
			Annotation a = f.getDeclaredAnnotation(TaskAttribute.class);
			if (a == null)
				continue;
			TaskAttribute annotation = a.getAnnotation(TaskAttribute.class);
			addEditField(task, annotation, f);
		}
	}

	private void addEditField (Task task, TaskAttribute ann, Field field) {
		// prefer name from annotation if there is one
		String name = ann.name();
		if (name == null || name.length() == 0) {
			name = field.getName();
		}
		boolean req = ann.required();
		// TODO some decent way of marking, maybe just validator?
		if (req)
			name += " (R)";
		VisLabel label = new VisLabel(name);
		add(label);
		try {
			Actor validator = createValidator(task, field, req);
			if (validator != null) {
				add(validator);
			}
		} catch (ReflectionException e) {
			Gdx.app.error(TAG, "Validator creation failed for task " + task + ", field " + field, e);
		}
		row();
	}

	private Actor createValidator (final Task task, final Field field, final boolean req) throws ReflectionException {
		// TODO how do we handle required stuff?
		return BTEEditField.createEditField(task, field);
	}

	@Override public void reset () {
		clear();
	}
}
