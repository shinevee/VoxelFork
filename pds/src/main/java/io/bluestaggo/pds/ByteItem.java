package io.bluestaggo.pds;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public class ByteItem extends StructureItem {
	public byte value;

	public ByteItem() {
	}

	public ByteItem(int value) {
		this((byte) value);
	}

	public ByteItem(byte value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ByteItem byteItem = (ByteItem) o;
		return value == byteItem.value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public String getString() {
		return String.valueOf(value);
	}

	@Override
	public byte getByte() {
		return value;
	}

	@Override
	public short getShort() {
		return value;
	}

	@Override
	public int getInt() {
		return value;
	}

	@Override
	public long getLong() {
		return value;
	}

	@Override
	public float getFloat() {
		return value;
	}

	@Override
	public double getDouble() {
		return value;
	}

	@Override
	protected void read(DataInputStream stream) throws IOException {
		value = stream.readByte();
	}

	@Override
	protected void write(DataOutputStream stream) throws IOException {
		stream.writeByte(value);
	}
}
