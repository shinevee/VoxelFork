package io.bluestaggo.voxelthing.renderer.util;

import io.bluestaggo.voxelthing.renderer.Bindings;
import io.bluestaggo.voxelthing.renderer.world.WorldVertex;

public final class WorldPrimitives {
	private WorldPrimitives() {
		throw new AssertionError("No io.bluestaggo.voxelthing.renderer.util.WorldPrimitives instances for you!");
	}

	public static Bindings<WorldVertex> generateSphere(float radius, int rings, int sectors) {
		var sphere = new Bindings<>(WorldVertex.LAYOUT);

		double sectorStep = 2.0F * Math.PI / sectors;
		double ringStep = Math.PI / rings;

		for (int i = 0; i <= rings; ++i) {
			double ringAngle = Math.PI / 2.0f - i * ringStep;
			double xy = radius * Math.cos(ringAngle);
			float z = radius * (float)Math.sin(ringAngle);

			for (int j = 0; j <= sectors; ++j) {
				double sectorAngle = j * sectorStep;
				float x = (float)(xy * Math.cos(sectorAngle));
				float y = (float)(xy * Math.sin(sectorAngle));
				float u = (float)j / sectors;
				float v = (float)i / sectors;
				sphere.addVertex(new WorldVertex(x, y, z, 1.0f, 1.0f, 1.0f, u, v));
			}
		}

		for (int i = 0; i < rings; ++i) {
			int k1 = i * (sectors + 1);
			int k2 = k1 + sectors + 1;

			for (int j = 0; j < sectors; ++j, ++k1, ++k2) {
				if (i != 0) {
					sphere.addIndex(k1);
					sphere.addIndex(k2);
					sphere.addIndex(k1 + 1);
				}

				if (i != rings - 1) {
					sphere.addIndex(k1 + 1);
					sphere.addIndex(k2);
					sphere.addIndex(k2 + 1);
				}
			}
		}

		sphere.upload(false);
		return sphere;
	}
}
