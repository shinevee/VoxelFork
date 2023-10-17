package io.bluestaggo.voxelthing.world.chunk.layer;

import io.bluestaggo.voxelthing.math.MathUtil;
import io.bluestaggo.voxelthing.world.chunk.Chunk;

public class NibbleBlockLayer extends BlockLayer {
	private final byte[] data;

	public NibbleBlockLayer() {
		this(new byte[Chunk.AREA / 2]);
	}

	public NibbleBlockLayer(byte[] data) {
		this.data = data;
	}

	@Override
	public int getBlockId(int x, int z) {
		int i = MathUtil.index2D(x, z, Chunk.LENGTH);
		int hi = i / 2;
		if (i % 2 == 0) {
			return (data[hi] & 0xF0) >> 4;
		} else {
			return data[hi] & 0xF;
		}
	}

	@Override
	public void setBlockId(int x, int z, int id) {
		int i = MathUtil.index2D(x, z, Chunk.LENGTH);
		int hi = i / 2;
		if (i % 2 == 0) {
			data[hi] = (byte) ((id & 0xF) << 4 | data[hi] & 0xF);
		} else {
			data[hi] = (byte) (data[hi] & 0xF0 | id & 0xF);
		}
	}

	@Override
	protected int getMaxPaletteSize() {
		return 16;
	}

	@Override
	public BlockLayer expand() {
		return new ByteBlockLayer(this.getDataAsBytes());
	}

	public byte[] getDataAsBytes() {
		byte[] bytes = new byte[data.length * 2];
		for (int i = 0; i < data.length; i++) {
			bytes[i * 2] = (byte) ((data[i] & 0xF0) >> 4);
			bytes[i * 2 + 1] = (byte) (data[i] & 0xF);
		}
		return bytes;
	}

	public byte[] getRawData() {
		return data;
	}
}
