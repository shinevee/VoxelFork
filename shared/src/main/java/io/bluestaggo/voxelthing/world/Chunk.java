package io.bluestaggo.voxelthing.world;

import io.bluestaggo.voxelthing.world.block.Block;

public class Chunk {
	public static final int SIZE_POW2 = 5;
	public static final int LENGTH = 1 << SIZE_POW2;
	public static final int AREA = 1 << SIZE_POW2 * 2;
	public static final int VOLUME = 1 << SIZE_POW2 * 3;

	public final World world;
	public final int x, y, z;

	private final short[] blocks = new short[VOLUME];
	private boolean empty = true;

	public Chunk(World world, int x, int y, int z) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public static int arrayCoords(int x, int y, int z) {
		return (x << SIZE_POW2 | z) << SIZE_POW2 | y;
	}

	public int getBlockId(int x, int y, int z) {
		return blocks[arrayCoords(x, y, z)] & 0xFFFF;
	}

	public Block getBlock(int x, int y, int z) {
		return Block.fromId(getBlockId(x, y, z));
	}

	public void setBlockId(int x, int y, int z, int id) {
		blocks[arrayCoords(x, y, z)] = (short) id;
		if (id > 0) {
			empty = false;
		}
	}

	public void setBlock(int x, int y, int z, Block block) {
		setBlockId(x, y, z, block != null ? block.id : 0);
	}

	public boolean isEmpty() {
		return empty;
	}
}
