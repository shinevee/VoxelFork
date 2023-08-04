package io.bluestaggo.voxelthing.world;

public enum Direction {
	NORTH(0, 0, -1),
	SOUTH(0, 0, 1),
	WEST(-1, 0, 0),
	EAST(1, 0, 0),
	BOTTOM(0, -1, 0),
	TOP(0, 1, 0);

	public final int X, Y, Z;

	Direction(int x, int y, int z) {
		X = x;
		Y = y;
		Z = z;
	}
}
