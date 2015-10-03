package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic;

import com.artemis.*;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.math.Circle;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.CircleBounds;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.Transform;

/**
 * Created by PiotrJ on 31/08/15.
 */
@Wire
public class Finder extends Manager {
	protected ComponentMapper<Transform> mTransform;
	protected ComponentMapper<CircleBounds> mCircleBounds;
	TagManager tags;

	public Finder () {
	}

	public float dst2 (int src, String target) {
		Entity tagged = tags.getEntity(target);
		if (tagged == null) {
			return 9999;
		}
		return dst2(src, tagged.getId());
	}

	public float dst2 (int src, int target) {
		Transform transA = mTransform.getSafe(src);
		Transform transB = mTransform.getSafe(target);
		if (transA == null || transB == null) return 9999;

		return transA.pos.dst2(transB.pos);
	}

	public boolean overlaps (int src, String target, float radius) {
		Entity tagged = tags.getEntity(target);
		if (tagged == null) {
			return false;
		}
		return overlaps(src, tagged.getId(), radius);
	}

	Circle tmp = new Circle();
	public boolean overlaps (int src, int target, float radius) {
		CircleBounds srcCB = mCircleBounds.getSafe(src);
		CircleBounds tarCB = mCircleBounds.getSafe(target);
		if (srcCB == null || tarCB == null) return false;
		tmp.set(srcCB.bounds);
		tmp.setRadius(radius);
		return tmp.overlaps(tarCB.bounds);
	}
}
