package io.piotrjastrzebski.ecsclones.base.managers;

import com.artemis.*;
import io.piotrjastrzebski.ecsclones.base.components.Asset;

/**
 * Created by PiotrJ on 04/08/15.
 */
public class AssetManager extends Manager {
	ComponentMapper<Asset> mAsset;
	Aspect assetAspect;
	@Override protected void initialize () {
		super.initialize();
		assetAspect = Aspect.all(Asset.class).build(world);
	}

	@Override public void added (int eid) {
		if (assetAspect.isInterested(world.getEntity(eid))) {
			initAsset(mAsset.get(eid));
		}
	}

	private void initAsset (Asset asset) {

	}
}
