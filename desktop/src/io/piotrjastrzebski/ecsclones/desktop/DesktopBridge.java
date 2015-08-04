package io.piotrjastrzebski.ecsclones.desktop;

import io.piotrjastrzebski.ecsclones.base.PlatformBridge;
import org.lwjgl.opengl.Display;

/**
 * Created by PiotrJ on 04/08/15.
 */
public class DesktopBridge implements PlatformBridge {
	@Override public float getPixelScaleFactor () {
		return Display.getPixelScaleFactor();
	}
}
