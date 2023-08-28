package io.bluestaggo.voxelthing.world;

import io.bluestaggo.voxelthing.world.block.Block;

public interface IBlockAccess {
	Block getBlock(int x, int y, int z);

	default boolean isAir(int x, int y, int z) {
		return getBlock(x, y, z) == null;
	}
}
