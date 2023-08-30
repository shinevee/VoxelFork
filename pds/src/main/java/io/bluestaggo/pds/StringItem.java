package io.bluestaggo.pds;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public class StringItem extends StructureItem {
	public String value;

	public StringItem() {
	}

	public StringItem(String value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		StringItem item = (StringItem) o;
		return value.equals(item.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public String getString() {
		return value == null ? "" : value;
	}

	@Override
	protected void read(DataInputStream stream) throws IOException {
		value = readString(stream);
	}

	@Override
	protected void write(DataOutputStream stream) throws IOException {
		writeString(value, stream);
	}
}
