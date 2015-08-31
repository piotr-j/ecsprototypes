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


	public float dst2 (int idA, String tag) {
		Entity tagged = tags.getEntity(tag);
		Transform transA = mTransform.get(idA);
		Transform transB = mTransform.get(tagged);
		if (transA == null || transB == null) return 9999;

		return transA.pos.dst2(transB.pos);
	}
}
