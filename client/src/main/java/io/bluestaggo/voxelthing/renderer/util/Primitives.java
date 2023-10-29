package io.bluestaggo.voxelthing.renderer.util;

import io.bluestaggo.voxelthing.renderer.vertices.Bindings;

public abstract class Primitives<T extends Bindings> {
	private static WorldPrimitives world;
	private static Vector3fPrimitives vector3f;

	protected abstract T newBindings();

	protected abstract void addPosition(T bindings, float x, float y, float z);

	protected abstract void addColor(T bindings, float r, float g, float b);

	protected abstract void addUv(T bindings, float u, float v);

	public T generateSphere(T bindings, float radius, int rings, int sectors) {
		if (bindings == null) bindings = newBindings();

		bindings.clear();
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
				addPosition(bindings, x, y, z);
				addColor(bindings, 1.0f, 1.0f, 1.0f);
				addUv(bindings, u, v);
			}
		}

		for (int i = 0; i < rings; ++i) {
			int k1 = i * (sectors + 1);
			int k2 = k1 + sectors + 1;

			for (int j = 0; j < sectors; ++j, ++k1, ++k2) {
				if (i != 0) {
					bindings.addIndex(k1);
					bindings.addIndex(k2);
					bindings.addIndex(k1 + 1);
				}

				if (i != rings - 1) {
					bindings.addIndex(k1 + 1);
					bindings.addIndex(k2);
					bindings.addIndex(k2 + 1);
				}
			}
		}

		bindings.upload(false);
		return bindings;
	}

	public T generatePlane(T bindings) {
		return generatePlane(bindings, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f);
	}

	public T generatePlane(T bindings, float x, float z, float width, float length) {
		return generatePlane(bindings, x, z, width, length, 1.0f, 1.0f, 1.0f);
	}

	public T generatePlane(T bindings, float x, float z, float width, float length, float r, float g, float b) {
		if (bindings == null) bindings = newBindings();

		addPosition(bindings, width + x, 0.0f, length + z);
		addColor(bindings, r, g, b);
		addUv(bindings, 1.0f, 1.0f);

		addPosition(bindings, width + x, 0.0f, 0.0f);
		addColor(bindings, r, g, b);
		addUv(bindings, 1.0f, 0.0f);

		addPosition(bindings, 0.0f, 0.0f, 0.0f);
		addColor(bindings, r, g, b);
		addUv(bindings, 0.0f, 0.0f);

		addPosition(bindings, 0.0f, 0.0f, length + z);
		addColor(bindings, r, g, b);
		addUv(bindings, 0.0f, 1.0f);

		bindings.addIndices(0, 1, 2, 2, 3, 0);
		bindings.upload(false);
		return bindings;
	}

	public static WorldPrimitives inWorld() {
		if (world == null) {
			world = new WorldPrimitives();
		}
		return world;
	}

	public static Vector3fPrimitives ofVector3f() {
		if (vector3f == null) {
			vector3f = new Vector3fPrimitives();
		}
		return vector3f;
	}
}
