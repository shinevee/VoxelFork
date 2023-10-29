package io.bluestaggo.voxelthing.renderer.vertices;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL33C.*;

public class VertexType {
	private static final Map<Integer, Integer> TYPE_TO_STRIDE = new HashMap<>();

	static {
		TYPE_TO_STRIDE.put(GL_BYTE, 1);
		TYPE_TO_STRIDE.put(GL_UNSIGNED_BYTE, 1);
		TYPE_TO_STRIDE.put(GL_SHORT, 2);
		TYPE_TO_STRIDE.put(GL_UNSIGNED_SHORT, 2);
		TYPE_TO_STRIDE.put(GL_INT, 4);
		TYPE_TO_STRIDE.put(GL_UNSIGNED_INT, 4);
		TYPE_TO_STRIDE.put(GL_HALF_FLOAT, 2);
		TYPE_TO_STRIDE.put(GL_FLOAT, 4);
	}

	public static final VertexType VECTOR2F = new VertexType(GL_FLOAT, 2);
	public static final VertexType VECTOR3F = new VertexType(GL_FLOAT, 3);
	public static final VertexType COLOR3F = new VertexType(GL_FLOAT, 3, true);
	public static final VertexType COLOR3B = new VertexType(GL_UNSIGNED_BYTE, 3, true);

	public final int type;
	public final int size;
	public final boolean normalized;
	public final int stride;

	public VertexType(int type, int size) {
		this(type, size, false);
	}

	public VertexType(int type, int size, boolean normalized) {
		this.type = type;
		this.size = size;
		this.normalized = normalized;
		this.stride = size * TYPE_TO_STRIDE.get(type);
	}
}
