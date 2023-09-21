package io.bluestaggo.voxelthing.settings;

import io.bluestaggo.voxelthing.gui.control.GuiControl;
import io.bluestaggo.voxelthing.gui.control.TextBox;
import io.bluestaggo.voxelthing.gui.screen.GuiScreen;

public class TextBoxSetting extends Setting<String> {
	public TextBoxSetting(String category, String name, String value) {
		super(category, name, value);
	}

	@Override
	public GuiControl getControl(GuiScreen screen) {
		return new TextBox(screen)
				.withText(value)
				.alignedAt(0.5f, 0.0f)
				.alignedSize(0.5f, 1.0f);
	}

	@Override
	public void handleControl(GuiControl control) {
		if (control instanceof TextBox labeledButton) {
			value = labeledButton.text;
		}
	}
}
