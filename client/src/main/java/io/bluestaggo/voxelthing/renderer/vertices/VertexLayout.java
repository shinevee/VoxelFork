package io.bluestaggo.voxelthing.renderer.vertices;

import io.bluestaggo.voxelthing.util.FloatList;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Arrays;

import static org.lwjgl.opengl.GL33C.*;

public class VertexLayout {
	public static final VertexLayout WORLD = new VertexLayout(
			VertexType.VECTOR3F,
			VertexType.COLOR3F,
			VertexType.VECTOR2F
	);
	public static final VertexLayout SCREEN = new VertexLayout(
			VertexType.VECTOR2F,
			VertexType.COLOR3F,
			VertexType.VECTOR2F
	);

	public final boolean floatOnly;
	private final VertexType[] vertexTypes;
	private int vertexSize;

	public VertexLayout(VertexType... vertexTypes) {
		this.vertexTypes = vertexTypes;
		this.floatOnly = Arrays.stream(vertexTypes).allMatch(vt -> vt.type == GL_FLOAT);
		this.vertexSize = Arrays.stream(vertexTypes)
				.mapToInt(vt -> vt.size)
				.sum();
	}

	public int getVertexSize() {
		return vertexSize;
	}

	public int genBuffer(int vao) {
		int buffer = glGenBuffers();
		glBindVertexArray(vao);

		int stride = Arrays.stream(vertexTypes)
				.mapToInt(vt -> vt.stride)
				.sum();
		int size = 0;
		glBindBuffer(GL_ARRAY_BUFFER, buffer);
		for (int i = 0; i < vertexTypes.length; i++) {
			VertexType type = vertexTypes[i];
			glVertexAttribPointer(i, type.size, type.type, type.normalized, stride, size);
			glEnableVertexAttribArray(i);
			size += type.stride;
		}

		return buffer;
	}

	public void floatBufferData(int vbo, FloatList data, boolean dynamic) {
		FloatBuffer buffer = RenderBuffers.getFloatDataBuffer(data.size());
		data.putToBuffer(buffer);
		buffer.flip();

		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, buffer, dynamic ? GL_DYNAMIC_DRAW : GL_STATIC_DRAW);
	}

	public void byteBufferData(int vbo, ByteBuffer buffer, boolean dynamic) {
		buffer.flip();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, buffer, dynamic ? GL_DYNAMIC_DRAW : GL_STATIC_DRAW);
		buffer.clear();
	}
}
