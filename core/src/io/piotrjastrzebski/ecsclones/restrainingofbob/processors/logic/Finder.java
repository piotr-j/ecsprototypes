package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import io.piotrjastrzebski.ecsclones.restrainingofbob.components.Transform;

/**
 * Created by PiotrJ on 31/08/15.
 */
@Wire
public class Finder extends EntitySystem {
	private final static String TAG = Finder.class.getSimpleName();

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
			Gdx.app.log(TAG, "Invalid tag " + target + "!");
			return 9999;
		}
		Transform transA = mTransform.get(src);
		Transform transB = mTransform.get(tagged);
		if (transA == null || transB == null) return 9999;

		return transA.pos.dst2(transB.pos);
	}
}
