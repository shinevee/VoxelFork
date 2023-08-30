package io.bluestaggo.pds;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class LongArrayItem extends StructureItem {
	public long[] value;

	public LongArrayItem() {
	}

	public LongArrayItem(long[] value) {
		this.value = value;
	}

	@Override
	public long[] getLongArray() {
		return value;
	}

	@Override
	public String toString() {
		return "<" + value.length + " longs>";
	}

	@Override
	protected void read(DataInputStream stream) throws IOException {
		int length = stream.readInt();
		value = new long[length / 8];
		byte[] bytes = stream.readNBytes(length);
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		buffer.asLongBuffer().get(value);
	}

	@Override
	protected void write(DataOutputStream stream) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(value.length * 8);
		buffer.asLongBuffer().put(value);

		stream.writeInt(value.length * 8);
		stream.write(buffer.array());
	}
}
