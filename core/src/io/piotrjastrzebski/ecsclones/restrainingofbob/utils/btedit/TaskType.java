package io.piotrjastrzebski.ecsclones.restrainingofbob.utils.btedit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by PiotrJ on 06/10/15.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TaskType {
	enum Type {
		ACTION, CONDITION, BRANCH, DECORATOR
	}
	Type value();
}
