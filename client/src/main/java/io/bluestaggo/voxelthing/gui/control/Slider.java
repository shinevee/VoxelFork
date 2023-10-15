package io.bluestaggo.voxelthing.gui.control;

import io.bluestaggo.voxelthing.gui.screen.GuiScreen;
import io.bluestaggo.voxelthing.math.MathUtil;
import io.bluestaggo.voxelthing.renderer.MainRenderer;
import io.bluestaggo.voxelthing.renderer.draw.Quad;
import io.bluestaggo.voxelthing.renderer.screen.Font;

public class Slider extends FocusableControl {
	public String text;
	public Font font;
	public float knobWidth = 6.0f;
	public float thickness = 4.0f;
	private float value;

	public Slider(GuiScreen screen) {
		super(screen);
	}

	public Slider withText(String text) {
		this.text = text;
		return this;
	}

	public Slider withFont(Font font) {
		this.font = font;
		return this;
	}

	public Slider setValue(float value) {
		this.value = value;
		return this;
	}

	public float getValue() {
		return value;
	}

	@Override
	public void draw() {
		MainRenderer r = screen.game.renderer;
		float sx = getScaledX();
		float sy = getScaledY();
		float sw = getScaledWidth();
		float sh = getScaledHeight();
		float barX = sx + value * (sw - knobWidth);

		if (font == null) {
			font = r.fonts.outlined;
		}

		r.draw2D.drawQuad(Quad.shared()
				.at(sx, sy + (sh - thickness) / 2.0f)
				.size(sw, thickness)
				.withColor(0.0f, 0.0f, 0.0f)
		);
		r.draw2D.drawQuad(Quad.shared()
				.at(barX, sy)
				.size(knobWidth, sh)
				.withColor(0.0f, 0.0f, 0.0f)
		);
		r.draw2D.drawQuad(Quad.shared()
				.at(barX + 1.0f, sy + 1.0f)
				.size(knobWidth - 2.0f, sh - 2.0f)
				.withColor(1.0f, 1.0f, 1.0f)
		);

		font.print(
				text,
				sx + (sw - font.getStringLength(text)) / 2.0f,
				sy + (sh - font.lineHeight) / 2.0f
		);
	}

	@Override
	public void onClick(int button, int mx, int my) {
		onMouseDragged(button, mx, my);
	}

	@Override
	public void onMouseDragged(int button, int mx, int my) {
		float sx = getScaledX() + knobWidth / 2.0f;
		float sw = getScaledWidth() - knobWidth;
		value = MathUtil.clamp((mx - sx) / sw);
	}

	@Override
	public boolean dragFocusOnly() {
		return true;
	}
}
