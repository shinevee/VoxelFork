package io.bluestaggo.voxelthing.world;

import org.joml.Vector3d;

public class BlockRaycast {
	public static final float STEP_DISTANCE = 1.0f / 16.0f;

	public final Vector3d position;
	public final Vector3d direction;
	public final double length;

	private boolean hit;
	private int hitX, hitY, hitZ;
	private Direction hitFace;

	public BlockRaycast(Vector3d position, Vector3d direction, double length) {
		this.position = position;
		this.direction = direction.normalize().mul(STEP_DISTANCE);
		this.length = length;
	}

	void setResult(int hitX, int hitY, int hitZ, Direction hitFace) {
		if (this.hitFace == null) {
			hit = true;
			this.hitX = hitX;
			this.hitY = hitY;
			this.hitZ = hitZ;
			this.hitFace = hitFace;
		}
	}

	public int getHitX() {
		return hitX;
	}

	public int getHitY() {
		return hitY;
	}

	public int getHitZ() {
		return hitZ;
	}

	public Direction getHitFace() {
		return hitFace;
	}

	public boolean blockHit() {
		return hit;
	}
}
