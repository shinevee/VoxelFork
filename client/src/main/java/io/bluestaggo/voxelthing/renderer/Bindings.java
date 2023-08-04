package io.bluestaggo.voxelthing.renderer;

import io.bluestaggo.voxelthing.util.IntList;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL33C.*;

public class Bindings<V> {
	private final VertexLayout<V> layout;
	private final int vao;
	private final int[] vbos;
	private final int ebo;
	private int vertexCount = 0;
	private int indexSize = 0;

	private final List<V> nextData = new ArrayList<>();
	private final IntList nextIndices = new IntList();

	public Bindings(VertexLayout<V> layout) {
		this.layout = layout;
		this.vao = glGenVertexArrays();
		this.vbos = layout.genBuffers(vao);
		this.ebo = glGenBuffers();
	}

	public void addVertex(V vertex) {
		nextData.add(vertex);
		vertexCount++;
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

	public void setData(List<V> data, IntList indices, boolean dynamic) {
		glBindVertexArray(vao);
		layout.bufferData(vbos, data, dynamic);
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
		glDeleteBuffers(vbos);
	}
}
