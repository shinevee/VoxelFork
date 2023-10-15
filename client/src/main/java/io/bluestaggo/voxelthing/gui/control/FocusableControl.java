package io.bluestaggo.voxelthing.gui.control;

import io.bluestaggo.voxelthing.gui.screen.GuiScreen;

public class FocusableControl extends Control {
	public FocusableControl(GuiScreen screen) {
		super(screen);
	}

	public void onKeyPressed(int key) {
	}

	public void onCharacterTyped(char c) {
	}

	@Override
	public void checkMouseClicked(int button, int mx, int my) {
		if (intersects(mx, my)) {
			screen.focusControl(this);
		}
	}

	public void onMouseDragged(int button, int mx, int my) {
	}

	public boolean dragFocusOnly() {
		return false;
	}
}
