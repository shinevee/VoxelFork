package io.bluestaggo.voxelthing.settings;

import io.bluestaggo.pds.*;
import io.bluestaggo.voxelthing.gui.control.GuiControl;
import io.bluestaggo.voxelthing.gui.screen.GuiScreen;

public abstract class Setting<T> {
	public final String category;
	public final String name;
	public final String saveName;
	protected T value;

	public Setting(String category, String name, T value) {
		this.category = category;
		this.name = name;
		this.value = value;

		String camelCategory = category.replace(" ", "");
		camelCategory = Character.toLowerCase(camelCategory.charAt(0)) + camelCategory.substring(1);

		String camelName = name.replace(" ", "");
		camelName = Character.toLowerCase(camelName.charAt(0)) + camelName.substring(1);

		this.saveName = camelCategory + ":" + camelName;
	}

	public T get() {
		return value;
	}

	public void set(T value) {
		this.value = value;
	}

	public StructureItem serialize() {
		if (value instanceof Boolean bool) {
			return new ByteItem(bool);
		} else if (value instanceof Integer integer) {
			return new IntItem(integer);
		} else if (value instanceof Float floating) {
			return new FloatItem(floating);
		} else if (value instanceof String string) {
			return new StringItem(string);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public void deserialize(StructureItem structureItem) {
		try {
			if (value instanceof Boolean) {
				value = (T) Boolean.valueOf(structureItem.getBoolean());
			} else if (value instanceof Integer) {
				value = (T) Integer.valueOf(structureItem.getInt());
			} else if (value instanceof Float) {
				value = (T) Float.valueOf(structureItem.getFloat());
			} else if (value instanceof String) {
				value = (T) structureItem.getString();
			}
		} catch (UnsupportedOperationException ignored) {
		}
	}

	public abstract GuiControl getControl(GuiScreen screen);

	public abstract void handleControl(GuiControl control);
}
