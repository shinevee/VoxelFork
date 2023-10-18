package io.bluestaggo.voxelthing.renderer.vertices;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class RenderBuffers {
	private static FloatBuffer floatDataBuffer;
	private static IntBuffer indexBuffer;

	public static FloatBuffer getFloatDataBuffer(int size) {
		if (floatDataBuffer == null || floatDataBuffer.capacity() < size) {
			MemoryUtil.memFree(floatDataBuffer);
			floatDataBuffer = MemoryUtil.memAllocFloat(size * 4);
		}

		floatDataBuffer.clear();
		return floatDataBuffer;
	}

	public static IntBuffer getIndexBuffer(int size) {
		if (indexBuffer == null || indexBuffer.capacity() < size) {
			MemoryUtil.memFree(indexBuffer);
			indexBuffer = MemoryUtil.memAllocInt(size * 4);
		}

		indexBuffer.clear();
		return indexBuffer;
	}

	public static void freeBuffers() {
		MemoryUtil.memFree(floatDataBuffer);
		MemoryUtil.memFree(indexBuffer);

		floatDataBuffer = null;
		indexBuffer = null;
	}
}
