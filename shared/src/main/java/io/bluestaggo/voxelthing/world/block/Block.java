package io.bluestaggo.voxelthing.world.block;

import io.bluestaggo.voxelthing.math.AABB;
import io.bluestaggo.voxelthing.world.Direction;
import io.bluestaggo.voxelthing.world.IBlockAccess;
import io.bluestaggo.voxelthing.world.block.texture.AllSidesTexture;
import io.bluestaggo.voxelthing.world.block.texture.GrassTexture;
import io.bluestaggo.voxelthing.world.block.texture.IBlockTexture;

import java.util.Arrays;

public class Block {
	public static final int TEXTURE_RES = 512;
	public static final int TEXTURE_ROWS = TEXTURE_RES / 16;
	public static final float TEXTURE_WIDTH = 1.0f / TEXTURE_ROWS;

	private static short nextId = 0;
	private static Block[] blocks = new Block[256];

	public static final Block STONE = new Block().withTex(1, 0);
	public static final Block GRASS = new Block().withTex(new GrassTexture(0, 1, 0, 0, 0, 2));
	public static final Block DIRT = new Block().withTex(0, 2);
	public static final Block BRICK = new Block().withTex(1, 2);

	public final short id;
	private IBlockTexture texture;

	public Block() {
		id = ++nextId;
		if (id == 0) {
			throw new IllegalStateException("Block ID limit of 65535 exceeded!");
		}
		if (id >= blocks.length) {
			blocks = Arrays.copyOf(blocks, blocks.length + 256);
		}
		blocks[id] = this;
	}

	public static Block fromId(short id) {
		if (id < 0 || id >= blocks.length) {
			return null;
		}
		return blocks[id & 0xFFFF];
	}

	protected Block withTex(int x, int y) {
		return withTex(new AllSidesTexture(x, y));
	}

	protected Block withTex(IBlockTexture texture) {
		this.texture = texture;
		return this;
	}

	public IBlockTexture getTexture() {
		return texture;
	}

	public boolean isFaceDrawn(IBlockAccess blockAccess, int x, int y, int z, Direction face) {
		return blockAccess.getBlockId(x, y, z) == 0;
	}

	public AABB getCollisionBox(int x, int y, int z) {
		return new AABB(x, y, z, x + 1, y + 1, z + 1);
	}
}
