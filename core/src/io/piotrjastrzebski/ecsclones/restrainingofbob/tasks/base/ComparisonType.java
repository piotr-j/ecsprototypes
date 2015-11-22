package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.base;

import com.badlogic.gdx.math.MathUtils;

/**
 * Created by EvilEntity on 17/11/2015.
 */
public enum ComparisonType implements Compare {
	GT {
		@Override public boolean compare (float a, float b) {
			return a > b;
		}

		@Override public boolean compare (int a, int b) {
			return a > b;
		}

		@Override public boolean compare (String a, String b) {
			return !(a == null || b == null) && a.compareTo(b) == 1;
		}
	}, LT {
		@Override public boolean compare (float a, float b) {
			return a < b;
		}

		@Override public boolean compare (int a, int b) {
			return a < b;
		}

		@Override public boolean compare (String a, String b) {
			return !(a == null || b == null) && a.compareTo(b) == -1;
		}
	}, GT_EQ {
		@Override public boolean compare (float a, float b) {
			return a >= b;
		}

		@Override public boolean compare (int a, int b) {
			return a >= b;
		}

		@Override public boolean compare (String a, String b) {
			return !(a == null || b == null) && a.compareTo(b) >= 0;
		}
	}, LT_EQ {
		@Override public boolean compare (float a, float b) {
			return a <= b;
		}

		@Override public boolean compare (int a, int b) {
			return a <= b;
		}

		@Override public boolean compare (String a, String b) {
			return !(a == null || b == null) && a.compareTo(b) <= 0;
		}
	}, EQ {
		@Override public boolean compare (float a, float b) {
			return MathUtils.isEqual(a, b);
		}

		@Override public boolean compare (int a, int b) {
			return a == b;
		}

		@Override public boolean compare (String a, String b) {
			return !(a == null || b == null) && a.compareTo(b) <= 0;
		}
	}, NEQ {
		@Override public boolean compare (float a, float b) {
			return !MathUtils.isEqual(a, b);
		}

		@Override public boolean compare (int a, int b) {
			return a == b;
		}

		@Override public boolean compare (String a, String b) {
			return !(a == null || b == null) && a.compareTo(b) == 0;
		}
	}
}
