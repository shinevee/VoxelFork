package io.bluestaggo.voxelthing.world.chunk.layer;

import io.bluestaggo.voxelthing.world.chunk.Chunk;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class SingleBlockLayer extends BlockLayer {
	private final int id;

	public SingleBlockLayer() {
		this(0);
	}

	public SingleBlockLayer(int id) {
		this.id = id;
	}

	@Override
	public int getBlockId(int x, int z) {
		return id;
	}

	@Override
	public void setBlockId(int x, int z, int id) {
	}

	@Override
	public boolean needsExpansion(int id) {
		return this.id != id;
	}

	@Override
	protected int getMaxPaletteSize() {
		return 0;
	}

	@Override
	public byte[] getRawData() {
		return ByteBuffer.allocate(4).putInt(id).array();
	}

	@Override
	public BlockLayer expand() {
		if (id == 0) {
			return new EmptyBlockLayer();
		} else if (id < 16) {
			byte[] array = new byte[Chunk.AREA / 2];
			Arrays.fill(array, (byte) (id << 4 | id));
			return new NibbleBlockLayer(array);
		} else if (id < 256) {
			byte[] array = new byte[Chunk.AREA];
			Arrays.fill(array, (byte) id);
			return new ByteBlockLayer(array);
		} else if (id < 65536) {
			short[] array = new short[Chunk.AREA];
			Arrays.fill(array, (short) id);
			return new ShortBlockLayer(array);
		}
		return super.expand();
	}
}
