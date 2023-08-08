package io.bluestaggo.voxelthing.renderer.vertices;

import io.bluestaggo.voxelthing.util.FloatList;
import io.bluestaggo.voxelthing.util.IntList;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL33C.*;

public class FloatBindings {
	private final FloatVertexLayout layout;
	private final int vao;
	private final int vbo;
	private final int ebo;
	private int nextIndexSize;
	private int vertexCount;
	private int indexSize;

	private final FloatList nextData = new FloatList();
	private final IntList nextIndices = new IntList();

	public FloatBindings(FloatVertexLayout layout) {
		this.layout = layout;
		this.vao = glGenVertexArrays();
		this.vbo = layout.genBuffer(vao);
		this.ebo = glGenBuffers();
	}

	public FloatBindings(FloatVertexType... vertexTypes) {
		this(new FloatVertexLayout(vertexTypes));
	}

	public void addVertex(float vertex) {
		nextData.add(vertex);
	}

	public void addVertices(float... vertices) {
		nextData.addAll(vertices);
	}

	public void addIndex(int index) {
		nextIndices.add(index);
		if (index + 1 > nextIndexSize) {
			nextIndexSize = index + 1;
		}
	}

	public void addIndices(int... indices) {
		int offset = nextIndexSize;
		for (int index : indices) {
			addIndex(index + offset);
		}
	}

	public void upload(boolean dynamic) {
		setData(nextData, nextIndices, dynamic);
		clear();
	}

	public void clear() {
		nextData.clear();
		nextIndices.clear();
		indexSize = nextIndexSize;
		nextIndexSize = 0;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public int getIndexSize() {
		return indexSize;
	}

	public void setData(FloatList data, IntList indices, boolean dynamic) {
		glBindVertexArray(vao);
		layout.bufferData(vbo, data, dynamic);
		vertexCount = indices.size();
		if (vertexCount == 0) vertexCount = data.size();

		IntBuffer intBuffer = MemoryUtil.memAllocInt(indices.size());
		indices.putToBuffer(intBuffer);
		intBuffer.flip();

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, intBuffer, dynamic ? GL_DYNAMIC_DRAW : GL_STATIC_DRAW);

		MemoryUtil.memFree(intBuffer);
	}

	public void draw() {
		glBindVertexArray(vao);
		if (indexSize > 0) {
			glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
		} else {
			glDrawArrays(GL_TRIANGLES, 0, vertexCount);
		}
	}

	public void unload() {
		glDeleteVertexArrays(vao);
		glDeleteBuffers(vbo);
		glDeleteBuffers(ebo);
	}
}
