package io.bluestaggo.voxelthing.renderer.vertices;

public class FloatVertexType {
	public final int size;
	public final boolean normalized;

	public FloatVertexType(int size) {
		this(size, false);
	}

	public FloatVertexType(int size, boolean normalized) {
		this.size = size;
		this.normalized = normalized;
	}

	public int getStride() {
		return size * 4;
	}
}
