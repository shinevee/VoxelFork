package io.bluestaggo.voxelthing.world.storage;

import io.bluestaggo.voxelthing.math.MathUtil;
import io.bluestaggo.voxelthing.world.BlockStorage;
import io.bluestaggo.voxelthing.world.block.Block;
import io.bluestaggo.voxelthing.world.chunk.Chunk;

import java.util.List;

public class ByteBlockStorage extends BlockStorage {
	private final byte[] blocks;

	public ByteBlockStorage() {
		super();
		blocks = new byte[Chunk.VOLUME];
	}

	public ByteBlockStorage(List<Block> palette) {
		super(palette);
		blocks = new byte[Chunk.VOLUME];
	}

	public ByteBlockStorage(List<Block> palette, byte[] bytes) {
		super(palette);
		blocks = bytes;
		updateBlockCounts();
	}

	public ByteBlockStorage(NibbleBlockStorage storage) {
		super(storage);
		blocks = new byte[Chunk.VOLUME];
		storage.copyBytes(blocks);
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

	@Override
	public byte[] getBytes() {
		return blocks;
	}
}
