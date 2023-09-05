package io.bluestaggo.voxelthing.world.storage;

import io.bluestaggo.voxelthing.world.BlockStorage;
import io.bluestaggo.voxelthing.world.block.Block;

import java.util.List;

public class EmptyBlockStorage extends BlockStorage {
	public EmptyBlockStorage() {
	}

	public EmptyBlockStorage(List<Block> palette, byte[] bytes) {
	}

	@Override
	protected int getBlockId(int x, int y, int z) {
		return 0;
	}

	@Override
	protected void setBlockId(int x, int y, int z, int id) {
	}

	@Override
	protected int getMaxPaletteSize() {
		return 0;
	}

	@Override
	public byte[] getBytes() {
		return new byte[0];
	}

	@Override
	public boolean needsExpansion(Block block) {
		return true;
	}

	@Override
	public BlockStorage expand() {
		return new NibbleBlockStorage();
	}
}
