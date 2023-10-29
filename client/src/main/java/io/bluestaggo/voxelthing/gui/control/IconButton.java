package io.bluestaggo.voxelthing.gui.control;

import io.bluestaggo.voxelthing.assets.Texture;
import io.bluestaggo.voxelthing.gui.Icon;
import io.bluestaggo.voxelthing.gui.screen.GuiScreen;
import io.bluestaggo.voxelthing.renderer.MainRenderer;
import io.bluestaggo.voxelthing.renderer.draw.Quad;

public class IconButton extends Control {
	public Icon icon;

	public IconButton(GuiScreen screen) {
		super(screen);
	}

	public IconButton withIcon(Icon icon) {
		this.icon = icon;
		return this;
	}

	@Override
	public void draw() {
		MainRenderer r = screen.game.renderer;
		float sx = getScaledX();
		float sy = getScaledY();
		float sw = getScaledWidth();
		float sh = getScaledHeight();
		Texture iconTexture = r.textures.getTexture("/assets/gui/icons.png");

		float u = iconTexture.uCoord(icon.texX * 16);
		float v = iconTexture.uCoord(icon.texY * 16);
		float u2 = u + iconTexture.uCoord(16);
		float v2 = v + iconTexture.vCoord(16);

		drawButtonBackground();
		r.draw2D.drawQuad(Quad.shared()
				.at(sx + (sw - 16.0f) / 2.0f, sy + (sh - 16.0f) / 2.0f)
				.size(16.0f, 16.0f)
				.withTexture(iconTexture)
				.withUV(u, v, u2, v2)
		);
	}
}
