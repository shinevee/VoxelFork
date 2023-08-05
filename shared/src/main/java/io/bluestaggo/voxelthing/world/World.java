package io.bluestaggo.voxelthing.world;

import io.bluestaggo.voxelthing.world.block.Block;
import io.bluestaggo.voxelthing.world.generation.GenCache;
import io.bluestaggo.voxelthing.world.generation.GenerationInfo;

import java.util.Random;

public class World {
	private final ChunkStorage chunkStorage;
	private final GenCache genCache;
	private final Random random = new Random();

	public final long seed = random.nextLong();

	public World() {
		chunkStorage = new ChunkStorage(this);
		genCache = new GenCache(this);
	}

	protected void debugChunk() {
		int range = 1 << ChunkStorage.RADIUS_POW2 >> 1;
		for (int x = -range; x <= range; x++) {
			for (int z = -range; z <= range; z++) {
				Chunk chunk = chunkStorage.newChunkAt(x, 0, z);

				for (int xx = 0; xx < 32; xx++) {
					for (int zz = 0; zz < 32; zz++) {
						for (int yy = 0; yy < 32; yy++) {
							Block block = Block.STONE;
							if ((x + z) % 2 ==0) {
								block = Block.BRICK;
							} else {
								if (yy >= 4) {
									block = Block.DIRT;
								}
								if (yy == 31) {
									block = Block.GRASS;
								}
								if (random.nextInt(2) == 0) {
									block = null;
								}
							}
							chunk.setBlock(xx, yy, zz, block);
						}
					}
				}
			}
		}
	}

	public Chunk getChunkAt(int x, int y, int z) {
		return chunkStorage.getChunkAt(x, y, z);
	}

	public boolean chunkExists(int x, int y, int z) {
		return getChunkAt(x, y, z) != null;
	}

	public Chunk getChunkAtBlock(int x, int y, int z) {
		return getChunkAt(
				Math.floorDiv(x, Chunk.LENGTH),
				Math.floorDiv(y, Chunk.LENGTH),
				Math.floorDiv(z, Chunk.LENGTH)
		);
	}

	public boolean chunkExistsAtBlock(int x, int y, int z) {
		return getChunkAtBlock(x, y, z) != null;
	}

	public Block getBlock(int x, int y, int z) {
		Chunk chunk = getChunkAtBlock(x, y, z);
		if (chunk == null) {
			return null;
		}
		return chunk.getBlock(
				Math.floorMod(x, Chunk.LENGTH),
				Math.floorMod(y, Chunk.LENGTH),
				Math.floorMod(z, Chunk.LENGTH)
		);
	}

	public short getBlockId(int x, int y, int z) {
		Chunk chunk = getChunkAtBlock(x, y, z);
		if (chunk == null) {
			return 0;
		}
		return chunk.getBlockId(
				Math.floorMod(x, Chunk.LENGTH),
				Math.floorMod(y, Chunk.LENGTH),
				Math.floorMod(z, Chunk.LENGTH)
		);
	}

	public void setBlock(int x, int y, int z, Block block) {
		Chunk chunk = getChunkAtBlock(x, y, z);
		if (chunk == null) {
			return;
		}
		chunk.setBlock(
				Math.floorMod(x, Chunk.LENGTH),
				Math.floorMod(y, Chunk.LENGTH),
				Math.floorMod(z, Chunk.LENGTH),
				block
		);
		onBlockUpdate(x, y, z);
	}

	public void loadChunkAt(int cx, int cy, int cz) {
		if (chunkStorage.getChunkAt(cx, cy, cz) != null) {
			return;
		}

		Chunk chunk = chunkStorage.newChunkAt(cx, cy, cz);
		GenerationInfo genInfo = genCache.getGenerationAt(cx, cz);

		for (int x = 0; x < Chunk.LENGTH; x++) {
			for (int z = 0; z < Chunk.LENGTH; z++) {
				float height = genInfo.getHeight(x, z);

				for (int y = 0; y < Chunk.LENGTH; y++) {
					int yy = cy * Chunk.LENGTH + y;
					boolean cave = yy < height && genInfo.getCave(x, yy, z);
					Block block = null;

					if (!cave) {
						if (yy < height - 4) {
							block = Block.STONE;
						} else if (yy < height - 1) {
							block = Block.DIRT;
						} else if (yy < height) {
							block = Block.GRASS;
						}
					}

					if (block != null) {
						chunk.setBlock(x, y, z, block);
					}
				}
			}
		}

		onChunkAdded(cx, cy, cz);
	}

	public void onBlockUpdate(int x, int y, int z) {
	}

	public void onChunkAdded(int x, int y, int z) {
	}
}
