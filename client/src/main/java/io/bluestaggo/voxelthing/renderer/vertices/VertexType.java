package io.bluestaggo.voxelthing.renderer.vertices;

import io.bluestaggo.voxelthing.renderer.screen.ScreenVertex;
import io.bluestaggo.voxelthing.renderer.world.WorldVertex;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL33C.*;

public enum VertexType {
	UNKNOWN(0, 0, 0, null) {
		@Override
		public void bufferData(List<?> data, Class<?> dataType, boolean dynamic) {
			throw new IllegalCallerException("Cannot assign buffer data with unknown type!");
		}
	},
	VECTOR3F(3, GL_FLOAT, 12, Vector3f.class) {
		@Override
		@SuppressWarnings("unchecked")
		public void bufferData(List<?> data, Class<?> dataType, boolean dynamic) {
			if (!Vector3f.class.isAssignableFrom(dataType)) {
				throw new ClassCastException("Data is not Vector3f!");
			}
			bufferData((List<Vector3f>) data, dynamic);
		}

		private void bufferData(List<Vector3f> data, boolean dynamic) {
			FloatBuffer floatBuffer = MemoryUtil.memAllocFloat(data.size() * 3);
			for (Vector3f v : data) {
				floatBuffer.put(v.x);
				floatBuffer.put(v.y);
				floatBuffer.put(v.z);
			}
			floatBuffer.flip();
			glBufferData(GL_ARRAY_BUFFER, floatBuffer, dynamic ? GL_DYNAMIC_DRAW : GL_STATIC_DRAW);
			MemoryUtil.memFree(floatBuffer);
		}
	},
	VECTOR2F(2, GL_FLOAT, 8, Vector2f.class) {
		@Override
		@SuppressWarnings("unchecked")
		public void bufferData(List<?> data, Class<?> dataType, boolean dynamic) {
			if (!Vector2f.class.isAssignableFrom(dataType)) {
				throw new ClassCastException("Data is not Vector2f!");
			}
			bufferData((List<Vector2f>) data, dynamic);
		}

		private void bufferData(List<Vector2f> data, boolean dynamic) {
			FloatBuffer floatBuffer = MemoryUtil.memAllocFloat(data.size() * 2);
			for (Vector2f v : data) {
				floatBuffer.put(v.x);
				floatBuffer.put(v.y);
			}
			floatBuffer.flip();
			glBufferData(GL_ARRAY_BUFFER, floatBuffer, dynamic ? GL_DYNAMIC_DRAW : GL_STATIC_DRAW);
			MemoryUtil.memFree(floatBuffer);
		}
	},
	WORLDVERTEX(8, GL_FLOAT, 32, WorldVertex.class) {
		@Override
		@SuppressWarnings("unchecked")
		public void bufferData(List<?> data, Class<?> dataType, boolean dynamic) {
			if (!WorldVertex.class.isAssignableFrom(dataType)) {
				throw new ClassCastException("Data is not WorldVertex!");
			}
			bufferData((List<WorldVertex>) data, dynamic);
		}

		private void bufferData(List<WorldVertex> data, boolean dynamic) {
			FloatBuffer floatBuffer = MemoryUtil.memAllocFloat(data.size() * 8);
			for (WorldVertex v : data) {
				floatBuffer.put(v.position.x);
				floatBuffer.put(v.position.y);
				floatBuffer.put(v.position.z);
				floatBuffer.put(v.color.x);
				floatBuffer.put(v.color.y);
				floatBuffer.put(v.color.z);
				floatBuffer.put(v.uv.x);
				floatBuffer.put(v.uv.y);
			}
			floatBuffer.flip();
			glBufferData(GL_ARRAY_BUFFER, floatBuffer, dynamic ? GL_DYNAMIC_DRAW : GL_STATIC_DRAW);
			MemoryUtil.memFree(floatBuffer);
		}
	},
	SCREENVERTEX(7, GL_FLOAT, 28, ScreenVertex.class) {
		@Override
		@SuppressWarnings("unchecked")
		public void bufferData(List<?> data, Class<?> dataType, boolean dynamic) {
			if (!ScreenVertex.class.isAssignableFrom(dataType)) {
				throw new ClassCastException("Data is not ScreenVertex!");
			}
			bufferData((List<ScreenVertex>) data, dynamic);
		}

		private void bufferData(List<ScreenVertex> data, boolean dynamic) {
			FloatBuffer floatBuffer = MemoryUtil.memAllocFloat(data.size() * 8);
			for (ScreenVertex v : data) {
				floatBuffer.put(v.position.x);
				floatBuffer.put(v.position.y);
				floatBuffer.put(v.color.x);
				floatBuffer.put(v.color.y);
				floatBuffer.put(v.color.z);
				floatBuffer.put(v.uv.x);
				floatBuffer.put(v.uv.y);
			}
			floatBuffer.flip();
			glBufferData(GL_ARRAY_BUFFER, floatBuffer, dynamic ? GL_DYNAMIC_DRAW : GL_STATIC_DRAW);
			MemoryUtil.memFree(floatBuffer);
		}
	};

	private static final Map<Class<?>, VertexType> CLASS_MAP = new HashMap<>();
	public final int size, glType, stride;
	public final Class<?> type;

	VertexType(int size, int glType, int stride, Class<?> type) {
		this.size = size;
		this.glType = glType;
		this.stride = stride;
		this.type = type;
	}

	public static VertexType fromClass(Class<?> type) {
		VertexType vertexType = CLASS_MAP.get(type);
		if (vertexType != null) return vertexType;
		return UNKNOWN;
	}

	public abstract void bufferData(List<?> data, Class<?> dataType, boolean dynamic);

	static {
		for (VertexType type : VertexType.values()) {
			CLASS_MAP.put(type.type, type);
		}
	}
}
