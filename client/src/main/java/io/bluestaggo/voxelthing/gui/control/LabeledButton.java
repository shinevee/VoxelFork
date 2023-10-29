package io.bluestaggo.voxelthing.gui.control;

import io.bluestaggo.voxelthing.gui.screen.GuiScreen;

public class LabeledButton extends Label {
	public LabeledButton(GuiScreen screen) {
		super(screen);
	}

	@Override
	public void draw() {
		drawButtonBackground();
		super.draw();
	}
}
