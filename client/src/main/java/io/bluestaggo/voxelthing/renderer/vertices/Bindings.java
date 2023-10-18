package io.bluestaggo.voxelthing.renderer.vertices;

import io.bluestaggo.voxelthing.util.IntList;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL15C.glDeleteBuffers;
import static org.lwjgl.opengl.GL15C.glGenBuffers;
import static org.lwjgl.opengl.GL30C.*;

public abstract class Bindings {
	protected final VertexLayout layout;
	protected final int vbo;
	protected long coordCount;
	private final int vao;
	private int ebo;
	private int verticesDrawn;
	private int indexCount;
	private int maxIndex;

	protected IntList nextIndices;

	public Bindings(VertexLayout layout) {
		this.layout = layout;
		vao = glGenVertexArrays();
		vbo = layout.genBuffer(vao);
 	}

	public void addIndex(int index) {
		if (nextIndices == null) {
			nextIndices = new IntList();
			ebo = glGenBuffers();
		}

		nextIndices.add(index);
		if (index + 1 > maxIndex) {
			maxIndex = index + 1;
		}
	}

	public void addIndices(int... indices) {
		int offset = maxIndex;
		for (int index : indices) {
			addIndex(index + offset);
		}
	}

	public final void upload(boolean dynamic) {
		glBindVertexArray(vao);
		uploadVertices(dynamic);
		uploadIndicies(dynamic);
		clear();
	}

	protected abstract void uploadVertices(boolean dynamic);

	protected void uploadIndicies(boolean dynamic) {
		if (nextIndices != null && ebo != 0) {
			IntBuffer intBuffer = RenderBuffers.getIndexBuffer(nextIndices.size());
			nextIndices.putToBuffer(intBuffer);
			intBuffer.flip();

			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, intBuffer, dynamic ? GL_DYNAMIC_DRAW : GL_STATIC_DRAW);
		}
	}

	public void clear() {
		indexCount = nextIndices != null ? nextIndices.size() : 0;
		verticesDrawn = indexCount > 0 ? indexCount : (int)(coordCount / layout.getVertexSize());
		maxIndex = 0;
		coordCount = 0;
		if (nextIndices != null) {
			nextIndices.clear();
		}
	}

	public void draw() {
		glBindVertexArray(vao);
		if (indexCount > 0 && ebo != 0) {
			glDrawElements(GL_TRIANGLES, verticesDrawn, GL_UNSIGNED_INT, 0);
		} else if (verticesDrawn > 0) {
			glDrawArrays(GL_TRIANGLES, 0, verticesDrawn);
		}
	}

	public void unload() {
		glDeleteVertexArrays(vao);
		glDeleteBuffers(vbo);
		if (ebo != 0) {
			glDeleteBuffers(ebo);
		}
	}
}
