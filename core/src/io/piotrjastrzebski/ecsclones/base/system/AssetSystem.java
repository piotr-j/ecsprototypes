package io.piotrjastrzebski.ecsclones.base.system;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import io.piotrjastrzebski.ecsclones.base.components.Asset;

/**
 * Created by PiotrJ on 04/08/15.
 */
public class AssetSystem extends EntityProcessingSystem {
	public AssetSystem () {
		super(Aspect.all(Asset.class));
	}

	@Override protected void initialize () {
		super.initialize();
	}

	@Override protected void inserted (Entity e) {

	}

	@Override protected void process (Entity e) {

	}
}
