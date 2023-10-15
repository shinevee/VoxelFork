package io.bluestaggo.voxelthing.settings;

import io.bluestaggo.voxelthing.gui.control.Control;
import io.bluestaggo.voxelthing.gui.control.LabeledButton;
import io.bluestaggo.voxelthing.gui.screen.GuiScreen;

public class ChoiceSetting extends Setting<Integer> {
	public final String[] choices;

	public ChoiceSetting(String category, String name, Integer value, String... choices) {
		super(category, name, value);
		this.choices = choices;
	}

	@Override
	public Control getControl(GuiScreen screen) {
		return new LabeledButton(screen)
				.withText(choices[value])
				.size(100, 0)
				.alignedAt(0.5f, 0.0f)
				.alignedSize(0.0f, 1.0f);
	}

	@Override
	public void handleControl(Control control) {
		if (++value >= choices.length) {
			value = 0;
		}

		if (control instanceof LabeledButton labeledButton) {
			labeledButton.text = choices[value];
		}
	}
}
