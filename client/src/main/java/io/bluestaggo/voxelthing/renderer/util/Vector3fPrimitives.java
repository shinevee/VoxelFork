package io.bluestaggo.voxelthing.renderer.util;

import io.bluestaggo.voxelthing.renderer.vertices.FloatBindings;
import io.bluestaggo.voxelthing.renderer.vertices.VertexType;

public class Vector3fPrimitives extends Primitives {
	@Override
	protected FloatBindings newBindings() {
		return new FloatBindings(VertexType.VECTOR3F);
	}

	@Override
	protected void addPosition(FloatBindings bindings, float x, float y, float z) {
		bindings.put(x, y, z);
	}

	@Override
	protected void addColor(FloatBindings bindings, float r, float g, float b) {
	}

	@Override
	protected void addUv(FloatBindings bindings, float u, float v) {
	}
}
