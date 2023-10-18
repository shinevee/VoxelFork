package io.bluestaggo.voxelthing.renderer.vertices;

import io.bluestaggo.voxelthing.util.FloatList;

public class FloatBindings extends Bindings {
	private final FloatList nextData = new FloatList();

	public FloatBindings(VertexLayout layout) {
		super(layout);
		if (!layout.floatOnly) {
			throw new IllegalArgumentException("FloatBindings only accepts float data");
		}
 	}

	public FloatBindings(VertexType... vertexTypes) {
		this(new VertexLayout(vertexTypes));
	}

	public FloatBindings put(float vertex) {
		nextData.add(vertex);
		coordCount++;
		return this;
	}

	public FloatBindings put(float... vertices) {
		nextData.addAll(vertices);
		coordCount += vertices.length;
		return this;
	}

	@Override
	protected void uploadVertices(boolean dynamic) {
		layout.floatBufferData(vbo, nextData, dynamic);
	}

	@Override
	public void clear() {
		nextData.clear();
		super.clear();
	}
}
