package io.bluestaggo.voxelthing.renderer.vertices;

public class VertexType {
	public static final VertexType VECTOR2F = new VertexType(2);
	public static final VertexType VECTOR3F = new VertexType(3);
	public static final VertexType COLOR3F = new VertexType(3, true);

	public final int size;
	public final boolean normalized;

	public VertexType(int size) {
		this(size, false);
	}

	public VertexType(int size, boolean normalized) {
		this.size = size;
		this.normalized = normalized;
	}

	public int getStride() {
		return size * 4;
	}
}
