package io.piotrjastrzebski.ecsclones.base.system;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.systems.IntervalIteratingSystem;
import com.artemis.systems.IteratingSystem;
import io.piotrjastrzebski.ecsclones.base.components.Asset;

/**
 * Created by PiotrJ on 04/08/15.
 */
public class AssetSystem extends IteratingSystem {
	public AssetSystem () {
		super(Aspect.all(Asset.class));
	}

	@Override protected void initialize () {
		super.initialize();
	}

	@Override protected void inserted (int eid) {

	}

	@Override protected void process (int entityId) {

	}
}
