package io.bluestaggo.voxelthing.math;

public class AABB {
	public double minX;
	public double minY;
	public double minZ;
	public double maxX;
	public double maxY;
	public double maxZ;

	public AABB() {
		this(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
	}

	public AABB(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		setBounds(minX, minY, minZ, maxX, maxY, maxZ, this);
	}

	public AABB setBounds(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, AABB result) {
		if (result == null) result = this;
		result.minX = minX;
		result.minY = minY;
		result.minZ = minZ;
		result.maxX = maxX;
		result.maxY = maxY;
		result.maxZ = maxZ;
		return result;
	}

	public AABB offset(double x, double y, double z, AABB result) {
		if (result == null) result = this;
		result.minX = minX + x;
		result.minY = minY + y;
		result.minZ = minZ + z;
		result.maxX = maxX + x;
		result.maxY = maxY + y;
		result.maxZ = maxZ + z;
		return result;
	}

	public AABB expandToPoint(double x, double y, double z, AABB result) {
		if (result == null) result = this;
		result.minX = x < 0.0 ? minX + x : minX;
		result.minY = y < 0.0 ? minY + y : minY;
		result.minZ = z < 0.0 ? minZ + z : minZ;
		result.maxX = x > 0.0 ? maxX + x : maxX;
		result.maxY = y > 0.0 ? maxY + y : maxY;
		result.maxZ = z > 0.0 ? maxZ + z : maxZ;
		return result;
	}

	public double calcXOffset(AABB other, double off) {
		if (other.maxY <= minY || other.minY >= maxY
				|| other.maxZ <= minZ || other.minZ >= maxZ) {
			return off;
		}

		if (off > 0.0 && other.maxX <= minX && minX - other.maxX < off) {
			off = minX - other.maxX;
		}

		if (off < 0.0 && other.minX >= maxX && maxX - other.minX > off) {
			off = maxX - other.minX;
		}

		return off;
	}

	public double calcYOffset(AABB other, double off) {
		if (other.maxX <= minX || other.minX >= maxX
				|| other.maxZ <= minZ || other.minZ >= maxZ) {
			return off;
		}

		if (off > 0.0 && other.maxY <= minY && minY - other.maxY < off) {
			off = minY - other.maxY;
		}

		if (off < 0.0 && other.minY >= maxY && maxY - other.minY > off) {
			off = maxY - other.minY;
		}

		return off;
	}

	public double calcZOffset(AABB other, double off) {
		if (other.maxX <= minX || other.minX >= maxX
				|| other.maxY <= minY || other.minY >= maxY) {
			return off;
		}

		if (off > 0.0 && other.maxZ <= minZ && minZ - other.maxZ < off) {
			off = minZ - other.maxZ;
		}

		if (off < 0.0 && other.minZ >= maxZ && maxZ - other.minZ > off) {
			off = maxZ - other.minZ;
		}

		return off;
	}

	public boolean intersects(AABB other) {
		return other.maxX > minX && other.minX < maxX
				&& other.maxY > minY && other.minY < maxY
				&& other.maxZ > minZ && other.minZ < maxZ;
	}
}
