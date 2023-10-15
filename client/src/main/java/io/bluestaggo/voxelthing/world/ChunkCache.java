package io.bluestaggo.voxelthing.world;

import io.bluestaggo.voxelthing.math.MathUtil;
import io.bluestaggo.voxelthing.world.block.Block;
import io.bluestaggo.voxelthing.world.chunk.Chunk;
import io.bluestaggo.voxelthing.world.chunk.EmptyChunk;

public class ChunkCache implements IBlockAccess {
	private final World world;
	private final Chunk[] chunks = new Chunk[27];
	private int x, y, z;

	public ChunkCache(World world) {
		this.world = world;
	}

	public void moveTo(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;

		for (int xx = 0; xx < 3; xx++) {
			for (int yy = 0; yy < 3; yy++) {
				for (int zz = 0; zz < 3; zz++) {
					int i = MathUtil.index3D(xx, yy, zz, 3);
					chunks[i] = world.getOrLoadChunkAt(x + xx - 1, y + yy - 1, z + zz - 1);
				}
			}
		}
	}

	private Chunk getChunkAtBlock(int x, int y, int z) {
		int ix = (x >> Chunk.SIZE_POW2) - (this.x - 1);
		int iy = (y >> Chunk.SIZE_POW2) - (this.y - 1);
		int iz = (z >> Chunk.SIZE_POW2) - (this.z - 1);

		Chunk chunk;

		if (ix < 0 || ix > 2 || iy < 0 || iy > 2 || iz < 0 || iz > 2) {
			chunk = world.getChunkAtBlock(x, y, z);
		} else {
			chunk = chunks[MathUtil.index3D(ix, iy, iz, 3)];
		}

		if (chunk == null) {
			chunk = EmptyChunk.instance;
		}

		return chunk;
	}

	@Override
	public Block getBlock(int x, int y, int z) {
		return getChunkAtBlock(x, y, z).getBlock(x & Chunk.LENGTH_MASK, y & Chunk.LENGTH_MASK, z & Chunk.LENGTH_MASK);
	}
}
