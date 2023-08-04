package io.bluestaggo.voxelthing.util;

public final class MathUtil {
	public static final float PI_F = (float)Math.PI;

	private MathUtil() {
		throw new AssertionError("No io.bluestaggo.voxelthing.util.MathUtil instances for you!");
	}

	public static float clamp(float x, float min, float max) {
		return Math.min(Math.max(x, min), max);
	}

	public static float threshold(float x, float min, float max) {
		return clamp((x - min) / (max - min), 0.0f, 1.0f);
	}

	public static float lerp(float a, float b, float t) {
		return a + (b - a) * t;
	}
}
