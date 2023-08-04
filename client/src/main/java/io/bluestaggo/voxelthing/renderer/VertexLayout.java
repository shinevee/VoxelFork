package io.bluestaggo.voxelthing.renderer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL33C.*;

public class VertexLayout<V> {
	private final boolean interleaved;

	private final Field[] fields;
	private final int bufferCount;

	private final VertexType[] vertexTypes;
	private final VertexType singleType;

	public VertexLayout(Class<V> vertexClass) {
		fields = Arrays.stream(vertexClass.getFields())
				.filter(f -> !Modifier.isStatic(f.getModifiers()))
				.toArray(Field[]::new);
		if (fields.length == 0) {
			throw new IllegalArgumentException("Class \"" + vertexClass.getName() + "\" has no fields!");
		}

		interleaved = Arrays.stream(fields)
				.map(Field::getType)
				.map(VertexType::fromClass)
				.map(vt -> vt.glType)
				.distinct()
				.count() == 1;
		if (interleaved) {
			bufferCount = 1;
			vertexTypes = null;
			singleType = VertexType.fromClass(vertexClass);
			if (singleType == VertexType.UNKNOWN) {
				throw new IllegalArgumentException("VertexLayout does not support type \""
						+ vertexClass.getName() + "\"!");
			}
		} else {
			bufferCount = fields.length;
			singleType = null;
			vertexTypes = new VertexType[bufferCount];

			for (int i = 0; i < bufferCount; i++) {
				Field field = fields[i];
				VertexType vertexType = VertexType.fromClass(field.getType());
				if (vertexType == VertexType.UNKNOWN) {
					throw new IllegalArgumentException("VertexLayout does not support field \"" + field.getName()
							+ "\" of type \"" + field.getType().getName() + "\"!");
				}
				vertexTypes[i] = vertexType;
			}
		}
	}

	public int[] genBuffers(int vao) {
		int[] buffers = new int[bufferCount];
		glGenBuffers(buffers);
		glBindVertexArray(vao);

		if (interleaved) {
			int stride = Arrays.stream(fields)
					.mapToInt(f -> VertexType.fromClass(f.getType()).stride)
					.sum();
			int size = 0;
			glBindBuffer(GL_ARRAY_BUFFER, buffers[0]);
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				VertexType type = VertexType.fromClass(field.getType());
				glVertexAttribPointer(i, type.size, type.glType,
						field.isAnnotationPresent(Normalized.class), stride, size);
				glEnableVertexAttribArray(i);
				size += type.stride;
			}
		} else {
			for (int i = 0; i < bufferCount; i++) {
				VertexType type = vertexTypes[i];
				Field field = fields[i];
				glBindBuffer(GL_ARRAY_BUFFER, buffers[i]);
				glEnableVertexAttribArray(i);
				glVertexAttribPointer(i, type.size, type.glType, field.isAnnotationPresent(Normalized.class), 0, 0);
			}
		}

		return buffers;
	}

	public void bufferData(int[] buffers, List<V> data, boolean dynamic) {
		if (buffers.length < bufferCount) {
			throw new IllegalArgumentException("Incorrect amount of buffers!");
		}

		if (interleaved) {
			glBindBuffer(GL_ARRAY_BUFFER, buffers[0]);
			singleType.bufferData(data, singleType.type, dynamic);
		} else {
			for (int i = 0; i < bufferCount; i++) {
				VertexType type = vertexTypes[i];
				Field field = fields[i];
				List<?> values = data.stream()
						.parallel()
						.map(v -> {
							try {
								return field.get(v);
							} catch (IllegalAccessException e) {
								throw new RuntimeException(e);
							}
						})
						.collect(Collectors.toList());
				glBindBuffer(GL_ARRAY_BUFFER, buffers[i]);
				type.bufferData(values, field.getType(), dynamic);
			}
		}
	}
}
