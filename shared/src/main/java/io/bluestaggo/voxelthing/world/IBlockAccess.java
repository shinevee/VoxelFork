package io.bluestaggo.voxelthing.world;

import io.bluestaggo.voxelthing.world.block.Block;

public interface IBlockAccess {
	Block getBlock(int x, int y, int z);
	short getBlockId(int x, int y, int z);
}
