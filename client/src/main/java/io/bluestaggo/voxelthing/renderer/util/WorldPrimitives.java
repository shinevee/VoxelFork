package io.bluestaggo.voxelthing.renderer.util;

import io.bluestaggo.voxelthing.renderer.vertices.FloatBindings;
import io.bluestaggo.voxelthing.renderer.vertices.VertexLayout;

public class WorldPrimitives extends Primitives {
	@Override
	protected FloatBindings newBindings() {
		return new FloatBindings(VertexLayout.WORLD);
	}

	@Override
	protected void addPosition(FloatBindings bindings, float x, float y, float z) {
		bindings.put(x, y, z);
	}

	@Override
	protected void addColor(FloatBindings bindings, float r, float g, float b) {
		bindings.put(r, g, b);
	}

	@Override
	protected void addUv(FloatBindings bindings, float u, float v) {
		bindings.put(u, v);
	}
}
