package io.bluestaggo.voxelthing.world;

import io.bluestaggo.voxelthing.world.block.Block;

public class EmptyChunk extends Chunk {
	public static final EmptyChunk instance = new EmptyChunk();

	private EmptyChunk() {
		super(null, 0, 0, 0);
	}

	@Override
	public short getBlockId(int x, int y, int z) {
		return 0;
	}

	@Override
	public Block getBlock(int x, int y, int z) {
		return null;
	}

	@Override
	public void setBlockId(int x, int y, int z, short id) {
	}

	@Override
	public void setBlock(int x, int y, int z, Block block) {
	}
}
