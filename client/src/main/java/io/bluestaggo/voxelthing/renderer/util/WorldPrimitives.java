package io.bluestaggo.voxelthing.renderer.util;
import io.bluestaggo.voxelthing.renderer.vertices.MixedBindings;
import io.bluestaggo.voxelthing.renderer.vertices.VertexLayout;

public class WorldPrimitives extends Primitives<MixedBindings> {
	@Override
	protected MixedBindings newBindings() {
		return new MixedBindings(VertexLayout.WORLD);
	}

	@Override
	protected void addPosition(MixedBindings bindings, float x, float y, float z) {
		bindings.put(x).put(y).put(z);
	}

	@Override
	protected void addColor(MixedBindings bindings, float r, float g, float b) {
		bindings.put((byte) (r * 255.0f)).put((byte) (g * 255.0f)).put((byte) (b * 255.0f));
	}

	@Override
	protected void addUv(MixedBindings bindings, float u, float v) {
		bindings.put(u).put(v);
	}
}
