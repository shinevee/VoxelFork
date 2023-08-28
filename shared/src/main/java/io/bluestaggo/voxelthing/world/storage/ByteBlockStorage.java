package io.bluestaggo.voxelthing.world.storage;

import io.bluestaggo.voxelthing.math.MathUtil;
import io.bluestaggo.voxelthing.world.BlockStorage;
import io.bluestaggo.voxelthing.world.Chunk;
import io.bluestaggo.voxelthing.world.block.Block;

import java.util.List;

public class ByteBlockStorage extends BlockStorage {
	private final byte[] blocks = new byte[Chunk.VOLUME];

	public ByteBlockStorage() {
		super();
	}

	public ByteBlockStorage(List<Block> palette) {
		super(palette);
	}

	public ByteBlockStorage(NibbleBlockStorage storage) {
		super(storage);
		byte[] nibbles = storage.getData();

		for (int i = 0; i < nibbles.length; i++) {
			blocks[i * 2] = (byte) ((nibbles[i] & 0xF0) >> 4);
			blocks[i * 2 + 1] = (byte) (nibbles[i] & 0xF);
		}
	}

	@Override
	protected int getBlockId(int x, int y, int z) {
		return blocks[MathUtil.index3D(x, y, z, Chunk.LENGTH)];
	}

	@Override
	protected void setBlockId(int x, int y, int z, int id) {
		blocks[MathUtil.index3D(x, y, z, Chunk.LENGTH)] = (byte)id;
	}

	@Override
	protected int getMaxPaletteSize() {
		return 256;
	}
}
