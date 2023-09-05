package io.bluestaggo.voxelthing.world.storage;

import io.bluestaggo.pds.CompoundItem;
import io.bluestaggo.voxelthing.world.Chunk;
import io.bluestaggo.voxelthing.world.World;

public class ChunkStorage {
	public static final int RADIUS_POW2 = 5;
	public static final int VOLUME = 1 << RADIUS_POW2 * 3;
	public static final int POS_MASK = (1 << RADIUS_POW2) - 1;

	private final World world;
	private final Chunk[] chunks = new Chunk[VOLUME];

	public ChunkStorage(World world) {
		this.world = world;
	}

	public static int storageCoords(int x, int y, int z) {
		x &= POS_MASK;
		y &= POS_MASK;
		z &= POS_MASK;
		return (x << RADIUS_POW2 | z) << RADIUS_POW2 | y;
	}

	public Chunk getChunkAt(int x, int y, int z) {
		Chunk chunk = chunks[storageCoords(x, y, z)];
		if (chunk == null || chunk.x != x || chunk.y != y || chunk.z != z) {
			return null;
		}

		return chunk;
	}

	public Chunk newChunkAt(int x, int y, int z) {
		Chunk chunk = getChunkAt(x, y, z);
		if (chunk != null) {
			return chunk;
		}

		chunk = chunks[storageCoords(x, y, z)];
		if (chunk != null) {
			chunk.onUnload();
			chunks[storageCoords(x, y, z)] = null;
		}
		chunk = new Chunk(world, x, y, z);
		chunks[storageCoords(x, y, z)] = chunk;
		return chunk;
	}

	public Chunk deserializeChunkAt(int x, int y, int z, CompoundItem item) {
		Chunk chunk = getChunkAt(x, y, z);
		if (chunk != null) {
			return chunk;
		}

		chunk = chunks[storageCoords(x, y, z)];
		if (chunk != null) {
			chunk.onUnload();
			chunks[storageCoords(x, y, z)] = null;
		}
		chunk = Chunk.deserialize(world, x, y, z, item);
		chunks[storageCoords(x, y, z)] = chunk;
		return chunk;
	}

	public void unloadAllChunks() {
		for (Chunk chunk : chunks) {
			if (chunk != null) {
				chunk.onUnload();
			}
		}
	}
}
