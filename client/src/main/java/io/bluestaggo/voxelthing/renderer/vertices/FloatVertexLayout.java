package io.bluestaggo.voxelthing.renderer.vertices;

import io.bluestaggo.voxelthing.util.FloatList;
import org.lwjgl.system.MemoryUtil;

import java.lang.reflect.Modifier;
import java.nio.FloatBuffer;
import java.util.Arrays;

import static org.lwjgl.opengl.GL33C.*;

public class FloatVertexLayout {
	private final FloatVertexType[] vertexTypes;

	public FloatVertexLayout(Class<?> vertexClass) {
		 this(Arrays.stream(vertexClass.getFields())
				 .filter(f -> !Modifier.isStatic(f.getModifiers()))
				 .filter(f -> VertexType.fromClass(f.getType()).glType == GL_FLOAT)
				 .map(f -> {
					 var vt = VertexType.fromClass(f.getType());
					 return new FloatVertexType(vt.size, f.isAnnotationPresent(Normalized.class));
				 })
				 .toArray(FloatVertexType[]::new));
	}

	public FloatVertexLayout(FloatVertexType... vertexTypes) {
		this.vertexTypes = vertexTypes;
	}

	public int genBuffer(int vao) {
		int buffer = glGenBuffers();
		glBindVertexArray(vao);

		int stride = Arrays.stream(vertexTypes)
				.mapToInt(FloatVertexType::getStride)
				.sum();
		int size = 0;
		glBindBuffer(GL_ARRAY_BUFFER, buffer);
		for (int i = 0; i < vertexTypes.length; i++) {
			FloatVertexType type = vertexTypes[i];
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
