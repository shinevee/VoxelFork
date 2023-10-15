package io.bluestaggo.voxelthing.settings;

import io.bluestaggo.voxelthing.gui.control.GuiControl;
import io.bluestaggo.voxelthing.gui.control.LabeledButton;
import io.bluestaggo.voxelthing.gui.screen.GuiScreen;

public class ToggleSetting extends Setting<Boolean> {
	public ToggleSetting(String category, String name, Boolean value) {
		super(category, name, value);
	}

	@Override
	public GuiControl getControl(GuiScreen screen) {
		return new LabeledButton(screen)
				.withText(value.toString())
				.size(100, 0)
				.alignedAt(0.5f, 0.0f)
				.alignedSize(0.0f, 1.0f);
	}

	@Override
	public void handleControl(GuiControl control) {
		value = !value;
		if (control instanceof LabeledButton labeledButton) {
			labeledButton.text = value ? "ON" : "OFF";
		}
	}
}
