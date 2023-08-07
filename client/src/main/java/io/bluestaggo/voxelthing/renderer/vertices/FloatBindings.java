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
	private int vertexCount = 0;
	private int indexSize = 0;

	private final FloatList nextData = new FloatList();
	private final IntList nextIndices = new IntList();

	public FloatBindings(FloatVertexLayout layout) {
		this.layout = layout;
		this.vao = glGenVertexArrays();
		this.vbo = layout.genBuffer(vao);
		this.ebo = glGenBuffers();
	}

	public void addVertex(float vertex) {
		nextData.add(vertex);
		vertexCount++;
	}

	public void addVertices(float... vertices) {
		nextData.add(vertices);
		vertexCount += vertices.length;
	}

	public void addIndex(int index) {
		nextIndices.add(index);
		if (index + 1 > indexSize) {
			indexSize = index + 1;
		}
	}

	public void addIndices(int... indices) {
		int offset = indexSize;
		for (int index : indices) {
			addIndex(index + offset);
		}
	}

	public void upload(boolean dynamic) {
		setData(nextData, nextIndices, dynamic);
		nextData.clear();
		nextIndices.clear();
		indexSize = 0;
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
		this.vertexCount = indices.size();

		IntBuffer intBuffer = MemoryUtil.memAllocInt(indices.size());
		indices.putToBuffer(intBuffer);
		intBuffer.flip();

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, intBuffer, dynamic ? GL_DYNAMIC_DRAW : GL_STATIC_DRAW);

		MemoryUtil.memFree(intBuffer);
	}

	public void draw() {
		glBindVertexArray(vao);
		glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
	}

	public void unload() {
		glDeleteVertexArrays(vao);
		glDeleteBuffers(vbo);
		glDeleteBuffers(ebo);
	}
}
