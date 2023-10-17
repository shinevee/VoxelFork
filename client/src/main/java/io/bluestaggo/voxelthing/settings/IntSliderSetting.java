package io.bluestaggo.voxelthing.settings;

import io.bluestaggo.voxelthing.gui.control.Control;
import io.bluestaggo.voxelthing.gui.control.Slider;
import io.bluestaggo.voxelthing.gui.screen.GuiScreen;

public class IntSliderSetting extends Setting<Integer> {
	public final int min;
	public final int max;

	public IntSliderSetting(String category, String name, Integer value, int min, int max) {
		super(category, name, value);
		this.min = min;
		this.max = max;
	}

	@Override
	public Control getControl(GuiScreen screen) {
		return new Slider(screen)
				.withText(getValueAsString())
				.setValue((float) (value - min) / (max - min))
				.size(100, 0)
				.alignedAt(0.5f, 0.0f)
				.alignedSize(0.0f, 1.0f);
	}

	@Override
	public void handleControl(Control control) {
		if (control instanceof Slider slider) {
			slider.setValue(Math.round(slider.getValue() * (max - min)) / (float) (max - min));
			value = (int) (slider.getValue() * (max - min) + min);
			slider.text = getValueAsString();
		}
	}
}
