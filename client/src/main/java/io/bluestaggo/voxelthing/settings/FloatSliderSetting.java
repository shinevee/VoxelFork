package io.bluestaggo.voxelthing.settings;

import io.bluestaggo.voxelthing.gui.control.Control;
import io.bluestaggo.voxelthing.gui.control.Slider;
import io.bluestaggo.voxelthing.gui.screen.GuiScreen;

public class FloatSliderSetting extends Setting<Float> {
	public final float min;
	public final float max;

	public FloatSliderSetting(String category, String name, Float value, float min, float max) {
		super(category, name, value);
		this.min = min;
		this.max = max;
	}

	@Override
	public Control getControl(GuiScreen screen) {
		return new Slider(screen)
				.withText(getValueAsString())
				.setValue(value / (max - min) - min)
				.size(100, 0)
				.alignedAt(0.5f, 0.0f)
				.alignedSize(0.0f, 1.0f);
	}

	@Override
	public void handleControl(Control control) {
		if (control instanceof Slider slider) {
			value = (slider.getValue() + min) * (max - min);
			slider.text = getValueAsString();
		}
	}
}
