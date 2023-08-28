package io.bluestaggo.voxelthing.math;

import org.joml.Vector3i;

import java.util.*;

public final class MathUtil {
	public static final float PI_F = (float)Math.PI;
	private static final Map<Integer, List<Vector3i>> SPHERE_POINT_LISTS = new HashMap<>();

	private MathUtil() {
		throw new AssertionError("No io.bluestaggo.voxelthing.util.MathUtil instances for you!");
	}

	public static float clamp(float x) {
		return clamp(x, 0.0f, 1.0f);
	}

	public static double clamp(double x) {
		return clamp(x, 0.0, 1.0);
	}

	public static int clamp(int x, int min, int max) {
		return Math.min(Math.max(x, min), max);
	}

	public static float clamp(float x, float min, float max) {
		return Math.min(Math.max(x, min), max);
	}

	public static double clamp(double x, double min, double max) {
		return Math.min(Math.max(x, min), max);
	}

	public static float threshold(float x, float min, float max) {
		return clamp((x - min) / (max - min), 0.0f, 1.0f);
	}

	public static float lerp(float a, float b, float t) {
		return a + (b - a) * t;
	}

	public static double lerp(double a, double b, double t) {
		return a + (b - a) * t;
	}

	public static float trilinear(float c000, float c001, float c010, float c011,
	                              float c100, float c101, float c110, float c111,
	                              float x, float y, float z) {
		float c00 = lerp(c000, c100, x);
		float c01 = lerp(c001, c101, x);
		float c10 = lerp(c010, c110, x);
		float c11 = lerp(c011, c111, x);

		float c0 = lerp(c00, c10, y);
		float c1 = lerp(c01, c11, y);

		return lerp(c0, c1, z);
	}

	public static int index3D(int x, int y, int z, int length) {
		return ((x * length) + y) * length + z;
	}

	public static int hexValue(char c) {
		return (c | 32) % 39 - 9;
	}

	public static float floorMod(float x, float y) {
		float mod = x % y;
		if (mod < 0) {
			mod += y;
		}
		return mod;
	}

	public static float squareOut(float x) {
		return 1.0f - (1.0f - x) * (1.0f - x);
	}

	public static List<Vector3i> getSpherePoints(int radius) {
		if (SPHERE_POINT_LISTS.containsKey(radius)) {
			return SPHERE_POINT_LISTS.get(radius);
		}

		List<Vector3i> points = new ArrayList<>();
		for (int x = -radius; x <= radius; x++) {
			for (int y = -radius; y <= radius; y++) {
				for (int z = -radius; z <= radius; z++) {
					points.add(new Vector3i(x, y, z));
				}
			}
		}

		points.sort(Comparator.comparingLong(Vector3i::lengthSquared));
		SPHERE_POINT_LISTS.put(radius, points);
		return points;
	}

	public static double sinPi(double x) {
		return Math.sin(x * Math.PI);
	}
}
