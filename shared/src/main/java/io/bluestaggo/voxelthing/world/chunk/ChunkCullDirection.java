package io.bluestaggo.voxelthing.world.chunk;

import io.bluestaggo.voxelthing.world.Direction;

public enum ChunkCullDirection {
	TOP_BOTTOM(Direction.TOP, Direction.BOTTOM),
	TOP_NORTH(Direction.TOP, Direction.NORTH),
	TOP_SOUTH(Direction.TOP, Direction.SOUTH),
	TOP_EAST(Direction.TOP, Direction.EAST),
	TOP_WEST(Direction.TOP, Direction.WEST),
	BOTTOM_NORTH(Direction.BOTTOM, Direction.NORTH),
	BOTTOM_SOUTH(Direction.BOTTOM, Direction.SOUTH),
	BOTTOM_EAST(Direction.BOTTOM, Direction.EAST),
	BOTTOM_WEST(Direction.BOTTOM, Direction.WEST),
	NORTH_SOUTH(Direction.NORTH, Direction.SOUTH),
	NORTH_WEST(Direction.NORTH, Direction.WEST),
	NORTH_EAST(Direction.NORTH, Direction.EAST),
	SOUTH_WEST(Direction.SOUTH, Direction.WEST),
	SOUTH_EAST(Direction.SOUTH, Direction.EAST),
	WEST_EAST(Direction.WEST, Direction.EAST);

	private static final ChunkCullDirection[] dirs = values();

	public final Direction faceA, faceB;
	public final int bitMask = 1 << ordinal();

	ChunkCullDirection(Direction faceA, Direction faceB) {
		this.faceA = faceA;
		this.faceB = faceB;
	}

	public static ChunkCullDirection get(Direction faceA, Direction faceB) {
		if (faceA == null || faceB == null) {
			throw new IllegalArgumentException("Cannot get cull direction from null faces");
		}

		for (ChunkCullDirection dir : dirs) {
			if (dir.faceA == faceA && dir.faceB == faceB || dir.faceB == faceA && dir.faceA == faceB) {
				return dir;
			}
		}

		return null;
	}
}
