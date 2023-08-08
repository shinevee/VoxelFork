package io.bluestaggo.voxelthing.renderer.vertices;

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
	private int nextIndexSize;
	private int vertexCount;
	private int indexSize;

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

	public void setData(List<V> data, IntList indices, boolean dynamic) {
		glBindVertexArray(vao);
		layout.bufferData(vbos, data, dynamic);
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
		glDeleteBuffers(vbos);
	}
}
