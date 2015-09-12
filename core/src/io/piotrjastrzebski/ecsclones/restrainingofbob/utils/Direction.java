package io.piotrjastrzebski.ecsclones.restrainingofbob.utils;

/**
 * Created by PiotrJ on 07/09/15.
 */
public enum Direction {
	UP(90), DOWN(270), LEFT(180), RIGHT(0);

	public final float angle;
	Direction (float angle) {
		this.angle = angle;
	}

	public static Direction of(float angle) {
		if (angle < 0) angle+=360;
		if (angle >= 360) angle -=360;
		for (Direction dir : values()) {
			if (angle >= dir.angle - 45 && angle < dir.angle + 45) {
				return dir;
			}
		}
		return UP;
	}

	@Override public String toString () {
		return super.toString() + "{" +
			"angle=" + angle +
			'}';
	}
}
