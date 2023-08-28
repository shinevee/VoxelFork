package io.bluestaggo.voxelthing.renderer.vertices;

import io.bluestaggo.voxelthing.util.FloatList;
import org.lwjgl.system.MemoryUtil;

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

	private final VertexType[] vertexTypes;

	public VertexLayout(VertexType... vertexTypes) {
		this.vertexTypes = vertexTypes;
	}

	public int genBuffer(int vao) {
		int buffer = glGenBuffers();
		glBindVertexArray(vao);

		int stride = Arrays.stream(vertexTypes)
				.mapToInt(VertexType::getStride)
				.sum();
		int size = 0;
		glBindBuffer(GL_ARRAY_BUFFER, buffer);
		for (int i = 0; i < vertexTypes.length; i++) {
			VertexType type = vertexTypes[i];
			glVertexAttribPointer(i, type.size, GL_FLOAT, type.normalized, stride, size);
			glEnableVertexAttribArray(i);
			size += type.getStride();
		}

		return buffer;
	}

	public void bufferData(int buffer, FloatList data, boolean dynamic) {
		glBindBuffer(GL_ARRAY_BUFFER, buffer);
		FloatBuffer floatBuffer = MemoryUtil.memAllocFloat(data.size() * 4);
		data.putToBuffer(floatBuffer);
		floatBuffer.flip();
		glBufferData(GL_ARRAY_BUFFER, floatBuffer, dynamic ? GL_DYNAMIC_DRAW : GL_STATIC_DRAW);
		MemoryUtil.memFree(floatBuffer);
	}
}
