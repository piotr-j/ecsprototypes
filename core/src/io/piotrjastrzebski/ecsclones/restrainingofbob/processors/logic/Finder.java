package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.Transform;

/**
 * Created by PiotrJ on 31/08/15.
 */
@Wire
public class Finder extends EntitySystem {
	protected ComponentMapper<Transform> mTransform;
	TagManager tags;

	public Finder () {
		super(Aspect.all(Transform.class));
		setPassive(true);
	}

	@Override protected void processSystem () {}


	public float dst2 (int src, String target) {
		Entity tagged = tags.getEntity(target);
		if (tagged == null) {
			return 9999;
		}
		return (dst2(src, tagged.id));
	}

	public float dst2 (int src, int target) {
		Transform transA = mTransform.get(src);
		Transform transB = mTransform.get(target);
		if (transA == null || transB == null) return 9999;

		return transA.pos.dst2(transB.pos);
	}
}
