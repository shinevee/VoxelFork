package io.bluestaggo.voxelthing.world;

import io.bluestaggo.voxelthing.world.block.Block;

public class Chunk implements IBlockAccess {
	public static final int SIZE_POW2 = 5;
	public static final int LENGTH = 1 << SIZE_POW2;
	public static final int LENGTH_MASK = (1 << SIZE_POW2) - 1;
	public static final int AREA = 1 << SIZE_POW2 * 2;
	public static final int VOLUME = 1 << SIZE_POW2 * 3;

	public final World world;
	public final int x, y, z;

	private short[] blocks;
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

	public int toGlobalX(int x) {
		return x + (this.x << Chunk.SIZE_POW2);
	}

	public int toGlobalY(int y) {
		return y + (this.y << Chunk.SIZE_POW2);
	}

	public int toGlobalZ(int z) {
		return z + (this.z << Chunk.SIZE_POW2);
	}

	@Override
	public short getBlockId(int x, int y, int z) {
		if (!containsLocal(x, y, z)) {
			return this.world.getBlockId(toGlobalX(x), toGlobalY(y), toGlobalZ(z));
		}

		if (blocks == null) {
			return 0;
		}

		return blocks[arrayCoords(x, y, z)];
	}

	@Override
	public Block getBlock(int x, int y, int z) {
		return Block.fromId(getBlockId(x, y, z));
	}

	public void setBlockId(int x, int y, int z, short id) {
		if (blocks == null) {
			if (id > 0) {
				blocks = new short[VOLUME];
			} else {
				return;
			}
		}

		blocks[arrayCoords(x, y, z)] = id;
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

	public boolean contains(int x, int y, int z) {
		x -= this.x * Chunk.LENGTH;
		y -= this.y * Chunk.LENGTH;
		z -= this.z * Chunk.LENGTH;
		return containsLocal(x, y, z);
	}

	public boolean containsLocal(int x, int y, int z) {
		return x >= 0 && x < Chunk.LENGTH
				&& y >= 0 && y < Chunk.LENGTH
				&& z >= 0 && z < Chunk.LENGTH;
	}
}
