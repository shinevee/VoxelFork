package io.bluestaggo.voxelthing.world.storage;

import io.bluestaggo.voxelthing.math.MathUtil;
import io.bluestaggo.voxelthing.world.BlockStorage;
import io.bluestaggo.voxelthing.world.block.Block;
import io.bluestaggo.voxelthing.world.chunk.Chunk;

import java.nio.ByteBuffer;
import java.util.List;

public class ShortBlockStorage extends BlockStorage {
	private final short[] blocks = new short[Chunk.VOLUME];

	public ShortBlockStorage() {
		super();
	}

	public ShortBlockStorage(List<Block> palette) {
		super(palette);
	}

	public ShortBlockStorage(List<Block> palette, byte[] bytes) {
		super(palette);
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		buffer.asShortBuffer().get(blocks);
		updateBlockCounts();
	}

	public ShortBlockStorage(ByteBlockStorage storage) {
		super(storage);
		ByteBuffer buffer = ByteBuffer.wrap(storage.getBytes());
		buffer.asShortBuffer().get(blocks);
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
		return 65536;
	}

	@Override
	public byte[] getBytes() {
		ByteBuffer buffer = ByteBuffer.allocate(blocks.length * 2);
		buffer.asShortBuffer().put(blocks);
		return buffer.array();
	}
}
