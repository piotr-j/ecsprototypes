package io.piotrjastrzebski.ecsclones.android;

import io.piotrjastrzebski.ecsclones.base.PlatformBridge;

/**
 * Created by PiotrJ on 04/08/15.
 */
public class AndroidBridge implements PlatformBridge {
	@Override public float getPixelScaleFactor () {
		return 1;
	}
}
