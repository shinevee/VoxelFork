package io.bluestaggo.pds;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ByteArrayItem extends StructureItem {
	public byte[] value;

	public ByteArrayItem() {
	}

	public ByteArrayItem(byte[] value) {
		this.value = value;
	}

	@Override
	public byte[] getByteArray() {
		return value;
	}

	@Override
	public String toString() {
		return "<" + value.length + " bytes>";
	}

	@Override
	protected void read(DataInputStream stream) throws IOException {
		int length = stream.readInt();
		value = stream.readNBytes(length);
	}

	@Override
	protected void write(DataOutputStream stream) throws IOException {
		stream.writeInt(value.length);
		stream.write(value);
	}
}
