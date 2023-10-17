package io.bluestaggo.voxelthing.settings;

import io.bluestaggo.pds.*;
import io.bluestaggo.voxelthing.gui.control.Control;
import io.bluestaggo.voxelthing.gui.screen.GuiScreen;

import java.util.Objects;
import java.util.function.Function;

public abstract class Setting<T> {
	public final String category;
	public final String name;
	public final String saveName;
	protected T value;

	private Function<T, String> textTransformer;
	private boolean modifiableOnDrag = true;

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

	public Setting<T> withTextTransformer(Function<T, String> textTransformer) {
		this.textTransformer = textTransformer;
		return this;
	}

	public Setting<T> setModifiableOnDrag(boolean modifiableOnDrag) {
		this.modifiableOnDrag = modifiableOnDrag;
		return this;
	}

	public T getValue() {
		return value;
	}

	public String getValueAsString() {
		if (textTransformer != null) {
			return textTransformer.apply(value);
		}

		if (value instanceof Float f) {
			return Float.toString((int) (f * 100.0f) / 100.0f);
		} else if (value instanceof Boolean b) {
			return b ? "ON" : "OFF";
		}

		return value.toString();
	}

	public void setValue(T value) {
		this.value = value;
	}

	public boolean isModifiableOnDrag() {
		return modifiableOnDrag;
	}

	public StructureItem serialize() {
		if (value instanceof Boolean bool) {
			return new ByteItem(bool);
		} else if (value instanceof Byte i8) {
			return new ByteItem(i8);
		} else if (value instanceof Short i16) {
			return new ShortItem(i16);
		} else if (value instanceof Integer i32) {
			return new IntItem(i32);
		} else if (value instanceof Long i64) {
			return new LongItem(i64);
		} else if (value instanceof Float f32) {
			return new FloatItem(f32);
		} else if (value instanceof Double f64) {
			return new DoubleItem(f64);
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

	public abstract Control getControl(GuiScreen screen);

	public abstract void handleControl(Control control);

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Setting<?> setting = (Setting<?>) o;
		return Objects.equals(category, setting.category) && Objects.equals(name, setting.name) && Objects.equals(value, setting.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(category, name, value);
	}
}
