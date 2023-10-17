package io.bluestaggo.voxelthing.settings;

import io.bluestaggo.voxelthing.gui.control.Control;
import io.bluestaggo.voxelthing.gui.control.LabeledButton;
import io.bluestaggo.voxelthing.gui.screen.GuiScreen;

public class ToggleSetting extends Setting<Boolean> {
	public ToggleSetting(String category, String name, Boolean value) {
		super(category, name, value);
	}

	@Override
	public Control getControl(GuiScreen screen) {
		return new LabeledButton(screen)
				.withText(getValueAsString())
				.size(100, 0)
				.alignedAt(0.5f, 0.0f)
				.alignedSize(0.0f, 1.0f);
	}

	@Override
	public void handleControl(Control control) {
		value = !value;
		if (control instanceof LabeledButton labeledButton) {
			labeledButton.text = getValueAsString();
		}
	}
}
