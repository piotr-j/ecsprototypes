package io.piotrjastrzebski.ecsclones.restrainingofbob.components;

import com.artemis.PooledComponent;

/**
 * Created by PiotrJ on 26/08/15.
 */
public class Facing extends PooledComponent {
	public enum Direction {
		UP(0), DOWN(180), LEFT(90), RIGHT(270);

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
	public Direction dir;

	public Facing () {
		reset();
	}

	public void set (float rot) {
		dir = Direction.of(rot);
	}

	@Override protected void reset () {
		dir = Direction.UP;
	}
}
