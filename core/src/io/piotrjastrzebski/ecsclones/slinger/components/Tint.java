package io.piotrjastrzebski.ecsclones.slinger.components;

import com.artemis.PooledComponent;
import com.badlogic.gdx.graphics.Color;

/**
 * Created by EvilEntity on 15/08/2015.
 */
public class Tint extends PooledComponent {
	public Color color = new Color(Color.WHITE);

	public Tint set (Color color) {
		this.color.set(color);
		return this;
	}

	public Tint set (float r, float g, float b) {
		color.r = r;
		color.g = g;
		color.b = b;
		return this;
	}

	public Tint set (float r, float g, float b, float a) {
		color.r = r;
		color.g = g;
		color.b = b;
		color.a = a;
		return this;
	}

	@Override protected void reset () {
		set(Color.WHITE);
	}
}
