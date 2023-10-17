package io.bluestaggo.voxelthing.world.chunk.layer;

import io.bluestaggo.voxelthing.math.MathUtil;
import io.bluestaggo.voxelthing.world.chunk.Chunk;

public class ByteBlockLayer extends BlockLayer {
	private final byte[] data;

	public ByteBlockLayer() {
		this(new byte[Chunk.AREA]);
	}

	public ByteBlockLayer(byte[] data) {
		this.data = data;
	}

	@Override
	public int getBlockId(int x, int z) {
		return data[MathUtil.index2D(x, z, Chunk.LENGTH)] & 0xFF;
	}

	@Override
	public void setBlockId(int x, int z, int id) {
		data[MathUtil.index2D(x, z, Chunk.LENGTH)] = (byte)id;
	}

	@Override
	protected int getMaxPaletteSize() {
		return 256;
	}

	@Override
	public byte[] getRawData() {
		return data;
	}

	@Override
	public BlockLayer expand() {
		return new ShortBlockLayer(this.getRawData());
	}
}
