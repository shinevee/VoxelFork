package io.bluestaggo.voxelthing.util;

public class OpenSimplex2Octaves {
	private interface FreqNoiseFunction {
		float get(long seed, double freq, double... coords);
	}

	@FunctionalInterface
	private interface NoiseFunction2 extends FreqNoiseFunction {
		float get2(long seed, double x, double y);

		default float get(long seed, double freq, double... coords) {
			return get2(seed, coords[0] * freq, coords[1] * freq);
		}
	}

	@FunctionalInterface
	private interface NoiseFunction3 extends FreqNoiseFunction {
		float get3(long seed, double x, double y, double z);

		default float get(long seed, double freq, double... coords) {
			return get3(seed, coords[0] * freq, coords[1] * freq, coords[2] * freq);
		}
	}

	@FunctionalInterface
	private interface OctaveFunction {
		float get(long seed, int octaves, double... coords);

		static OctaveFunction from(FreqNoiseFunction n) {
			return ((seed, octaves, coords) -> {
				float maxAmp = 0.0F;
				double amp = 1.0D;
				double freq = 1;
				float value = 0;
				for (int i = 0; i < octaves; i++) {
					value += n.get(seed, freq, coords) * amp;
					maxAmp += amp;
					amp /= 2.0D;
					freq *= 2.0D;
				}
				return value / maxAmp;
			});
		}
	}

	private static final OctaveFunction OCTAVES_NOISE_2 = OctaveFunction.from((NoiseFunction2) OpenSimplex2::noise2);
	private static final OctaveFunction OCTAVES_NOISE_3_XZ = OctaveFunction.from((NoiseFunction3) OpenSimplex2::noise3_ImproveXZ);

	public static float noise2(long seed, int octaves, double x, double y) {
		return OCTAVES_NOISE_2.get(seed, octaves, x, y);
	}

	public static float noise3_ImproveXZ(long seed, int octaves, double x, double y, double z) {
		return OCTAVES_NOISE_3_XZ.get(seed, octaves, x, y, z);
	}
}
