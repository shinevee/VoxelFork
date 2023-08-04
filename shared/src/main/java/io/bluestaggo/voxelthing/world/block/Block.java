package io.bluestaggo.voxelthing.world.block;

import io.bluestaggo.voxelthing.world.Direction;
import io.bluestaggo.voxelthing.world.World;

import java.util.Arrays;

public class Block {
	public static final int TEXTURE_RES = 512;
	public static final int TEXTURE_ROWS = TEXTURE_RES / 16;
	public static final float TEXTURE_WIDTH = 1.0f / TEXTURE_ROWS;

	private static int nextId = 0;
	private static Block[] blocks = new Block[256];

	public static final Block STONE = new Block().withTex(1, 0);
	public static final Block GRASS = new Block().withTex(0, 0);
	public static final Block DIRT = new Block().withTex(0, 2);
	public static final Block BRICK = new Block().withTex(1, 2);

	public final int id;
	private int texX, texY;

	public Block() {
		id = ++nextId;
		if (id >= blocks.length) {
			if (blocks.length * 2 > (1 << Short.SIZE)) {
				throw new IllegalStateException("Block ID limit of 65536 exceeded!");
			}
			blocks = Arrays.copyOf(blocks, blocks.length * 2);
		}
		blocks[id] = this;
	}

	public boolean isFaceDrawn(World world, int x, int y, int z, Direction face) {
		return world.getBlock(x, y, z) == null;
	}

	public Block withTex(int x, int y) {
		this.texX = x;
		this.texY = y;
		return this;
	}

	public int getTexX() {
		return texX;
	}

	public int getTexY() {
		return texY;
	}

	public static Block fromId(int id) {
		if (id < 0 || id >= blocks.length) {
			return null;
		}
		return blocks[id];
	}
}
