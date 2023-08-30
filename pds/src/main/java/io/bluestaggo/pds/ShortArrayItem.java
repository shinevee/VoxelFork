package io.bluestaggo.pds;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ShortArrayItem extends StructureItem {
	public short[] value;

	public ShortArrayItem() {
	}

	public ShortArrayItem(short[] value) {
		this.value = value;
	}

	@Override
	public short[] getShortArray() {
		return value;
	}

	@Override
	public String toString() {
		return "<" + value.length + " shorts>";
	}

	@Override
	protected void read(DataInputStream stream) throws IOException {
		int length = stream.readInt();
		value = new short[length / 2];
		byte[] bytes = stream.readNBytes(length);
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		buffer.asShortBuffer().get(value);
	}

	@Override
	protected void write(DataOutputStream stream) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(value.length * 2);
		buffer.asShortBuffer().put(value);

		stream.writeInt(value.length * 2);
		stream.write(buffer.array());
	}
}
