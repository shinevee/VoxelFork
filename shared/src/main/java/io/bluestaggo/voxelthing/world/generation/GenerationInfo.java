package io.bluestaggo.voxelthing.world.generation;

import io.bluestaggo.voxelthing.util.MathUtil;
import io.bluestaggo.voxelthing.util.OpenSimplex2Octaves;
import io.bluestaggo.voxelthing.world.Chunk;

public class GenerationInfo {
	public final int chunkX, chunkZ;
	private long randSeed;

	private final long caveSeed;

	private final float[] height;

	public GenerationInfo(long salt, int cx, int cz) {
		randSeed = salt;

		long seed = splitMix();
		caveSeed = splitMix();

		chunkX = cx;
		chunkZ = cz;

		final int baseHeightOctaves = 4;
		final double baseScale = 250.0;
		final float baseHeightScale = 8.0f;

		final int hillHeightOctaves = 3;
		final double hillScale = 250.0;
		final float hillHeightScale = 16.0f;
		final float hillHeightScaleMod = 4.0f;
		final float hillThresholdMin = -0.5f;
		final float hillThresholdMax = 1.0f;

		height = new float[Chunk.AREA];
		for (int x = 0; x < Chunk.LENGTH; x++) {
			for (int z = 0; z < Chunk.LENGTH; z++) {
				int xx = (cx * Chunk.LENGTH + x);
				int zz = (cz * Chunk.LENGTH + z);

				float baseHeight = OpenSimplex2Octaves.noise2(seed, baseHeightOctaves, xx / baseScale, zz / baseScale);
				float hill = OpenSimplex2Octaves.noise2(~seed, hillHeightOctaves, xx / hillScale, zz / hillScale);
				hill = 1.0f - (float) Math.cos(MathUtil.threshold(hill, hillThresholdMin, hillThresholdMax) * MathUtil.PI_F / 2.0f);

				float addedBaseHeight = baseHeightScale * MathUtil.lerp(1.0f, hillHeightScaleMod, hill);
				hill = hill * hillHeightScale;
				baseHeight = baseHeight * addedBaseHeight + addedBaseHeight;

				height[x + z * Chunk.LENGTH] = baseHeight + hill;
			}
		}
	}

	private long splitMix() {
		long z = (randSeed += 0x9e3779b97f4a7c15L);
	    z = (z ^ (z >>> 30)) * 0xbf58476d1ce4e5b9L;
        z = (z ^ (z >>> 27)) * 0x94d049bb133111ebL;
		return z ^ z >>> 31;
	}

	public float getHeight(int x, int z) {
		return height[x + z * Chunk.LENGTH];
	}

	public boolean getCave(int x, int yy, int z) {
		int xx = chunkX * Chunk.LENGTH + x;
		int zz = chunkZ * Chunk.LENGTH + z;

		final int cheeseOctaves = 3;
		final double cheeseScaleXZ = 100.0;
		final double cheeseScaleY = 50.0;
		final float cheeseMinDensity = -1.0f;
		final float cheeseMaxDensity = -0.3f;
		final float cheeseDensitySpread = 100.0f;
		final float cheeseDensitySurface = -0.5f;

		float cheese = OpenSimplex2Octaves.noise3_ImproveXZ(caveSeed, cheeseOctaves, xx / cheeseScaleXZ, yy / cheeseScaleY, zz / cheeseScaleXZ);
		float cheeseThreshold = MathUtil.clamp(-yy / cheeseDensitySpread + cheeseDensitySurface, cheeseMinDensity, cheeseMaxDensity);
		return cheese < cheeseThreshold;
	}
}
