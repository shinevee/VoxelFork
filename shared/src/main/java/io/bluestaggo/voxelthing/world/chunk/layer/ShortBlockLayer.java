package io.bluestaggo.voxelthing.world.chunk.layer;

import io.bluestaggo.voxelthing.math.MathUtil;
import io.bluestaggo.voxelthing.world.chunk.Chunk;

import java.nio.ByteBuffer;

public class ShortBlockLayer extends BlockLayer {
	private final short[] data;

	public ShortBlockLayer() {
		this(new short[Chunk.AREA]);
	}

	public ShortBlockLayer(short[] data) {
		this.data = data;
	}

	public ShortBlockLayer(byte[] data) {
		this();
		ByteBuffer buffer = ByteBuffer.wrap(data);
		buffer.asShortBuffer().get(this.data);
	}

	@Override
	public int getBlockId(int x, int z) {
		return data[MathUtil.index2D(x, z, Chunk.LENGTH)];
	}

	@Override
	public void setBlockId(int x, int z, int id) {
		data[MathUtil.index2D(x, z, Chunk.LENGTH)] = (byte)id;
	}

	@Override
	protected int getMaxPaletteSize() {
		return 65536;
	}

	@Override
	public byte[] getRawData() {
		ByteBuffer buffer = ByteBuffer.allocate(data.length * 2);
		buffer.asShortBuffer().put(data);
		return buffer.array();
	}
}
