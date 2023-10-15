package io.bluestaggo.voxelthing.settings;

import io.bluestaggo.voxelthing.gui.control.Control;
import io.bluestaggo.voxelthing.gui.control.TextBox;
import io.bluestaggo.voxelthing.gui.screen.GuiScreen;

public class TextBoxSetting extends Setting<String> {
	public TextBoxSetting(String category, String name, String value) {
		super(category, name, value);
	}

	@Override
	public Control getControl(GuiScreen screen) {
		return new TextBox(screen)
				.withText(value)
				.alignedAt(0.5f, 0.0f)
				.alignedSize(0.5f, 1.0f);
	}

	@Override
	public void handleControl(Control control) {
		if (control instanceof TextBox labeledButton) {
			value = labeledButton.text;
		}
	}
}
