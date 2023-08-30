package io.bluestaggo.pds;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class IntArrayItem extends StructureItem {
	public int[] value;

	public IntArrayItem() {
	}

	public IntArrayItem(int[] value) {
		this.value = value;
	}

	@Override
	public int[] getIntArray() {
		return value;
	}

	@Override
	public String toString() {
		return "<" + value.length + " ints>";
	}

	@Override
	protected void read(DataInputStream stream) throws IOException {
		int length = stream.readInt();
		value = new int[length / 4];
		byte[] bytes = stream.readNBytes(length);
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		buffer.asIntBuffer().get(value);
	}

	@Override
	protected void write(DataOutputStream stream) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(value.length * 4);
		buffer.asIntBuffer().put(value);

		stream.writeInt(value.length * 4);
		stream.write(buffer.array());
	}
}
