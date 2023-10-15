package io.bluestaggo.voxelthing.gui.control;

import io.bluestaggo.voxelthing.gui.screen.GuiScreen;
import io.bluestaggo.voxelthing.renderer.MainRenderer;
import io.bluestaggo.voxelthing.renderer.screen.Font;

public class Label extends Control {
	public String text;
	public Font font;
	public float textAlignX = 0.5f;
	public float textAlignY = 0.5f;

	public Label(GuiScreen screen) {
		super(screen);
	}

	public Label withText(String text) {
		this.text = text;
		return this;
	}

	public Label withFont(Font font) {
		this.font = font;
		return this;
	}

	public Control textAlignedAt(float textAlignX, float textAlignY) {
		this.textAlignX = textAlignX;
		this.textAlignY = textAlignY;
		return this;
	}

	@Override
	public void draw() {
		MainRenderer r = screen.game.renderer;
		float sx = getScaledX();
		float sy = getScaledY();
		float sw = getScaledWidth();
		float sh = getScaledHeight();

		if (font == null) {
			font = r.fonts.shadowed;
		}

		font.print(
				text,
				sx + (sw - font.getStringLength(text)) * textAlignX,
				sy + (sh - font.lineHeight) * textAlignY
		);
	}
}
