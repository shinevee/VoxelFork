package io.bluestaggo.voxelthing.math;

public final class MathUtil {
	public static final float PI_F = (float)Math.PI;

	private MathUtil() {
		throw new AssertionError("No io.bluestaggo.voxelthing.util.MathUtil instances for you!");
	}

	public static float clamp(float x) {
		return clamp(x, 0.0f, 1.0f);
	}

	public static double clamp(double x) {
		return clamp(x, 0.0, 1.0);
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
}
