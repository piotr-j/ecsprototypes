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

	@Override public void added (Entity e) {
		if (assetAspect.isInterested(e)) {
			initAsset(mAsset.get(e));
		}
	}

	private void initAsset (Asset asset) {

	}
}
