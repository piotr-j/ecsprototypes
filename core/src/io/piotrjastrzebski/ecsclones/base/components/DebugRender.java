package io.piotrjastrzebski.ecsclones.base.components;

import com.artemis.PooledComponent;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by PiotrJ on 04/08/15.
 */
public class DebugRender extends PooledComponent {
	public static final int TYPE_RECT = 0;
	public static final int TYPE_CIRCLE = 1;
	public int type;
	public Color color = new Color();
	@Override protected void reset () {
		color.set(Color.WHITE);
		type = TYPE_RECT;
	}
}
