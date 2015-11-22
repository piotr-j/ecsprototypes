package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic;

import com.artemis.*;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.Sort;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.CircleBounds;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.physics.Transform;

import java.util.Arrays;

/**
 * Created by PiotrJ on 31/08/15.
 */
@Wire
public class Finder extends Manager {
	protected ComponentMapper<Transform> mTransform;
	protected ComponentMapper<CircleBounds> mCircleBounds;
	TagManager tags;
	Groups groups;

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

	private IntArray found = new IntArray();
	private Vector2 dst = new Vector2();
	private FloatArray dsts = new FloatArray();

	public IntArray findTaggedWithin (int src, String target, float distance) {
		found.clear();
		IntBag entities = groups.getEntities(target);
		// can this ever be null? yes...
		CircleBounds srcCB = mCircleBounds.getSafe(src);
		if (entities.size() == 0 || srcCB == null) return found;
		tmp.set(srcCB.bounds);
		tmp.setRadius(distance);
		int[] data = entities.getData();
		dsts.ensureCapacity(entities.size());
		// reset size, no need to clear
		dsts.size = 0;
		for (int i = 0; i < entities.size(); i++) {
			int id = data[i];
			CircleBounds other = mCircleBounds.getSafe(id);
			if (other == null) continue;
			if (tmp.overlaps(other.bounds)) found.add(id);
			dsts.add(dst.set(tmp.x, tmp.y).dst2(other.bounds.x, other.bounds.y));
		}
		float[] a = dsts.items;
		for (int n = 0; n < entities.size(); n++) {
			for (int m = 0; m < 4 - n; m++) {
				if ((a[m] > a[m + 1])) {
					float swapFloat = a[m];
					a[m] = a[m + 1];
					a[m + 1] = swapFloat;
					int swapInt = data[m];
					data[m] = data[m + 1];
					data[m + 1] = swapInt;
				}
			}
		}
		found.reverse();
		return found;
	}
}
