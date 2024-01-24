package io.bluestaggo.voxelthing.renderer.vertices;

import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

public class MixedBindings extends Bindings {
	private ByteBuffer nextData = MemoryUtil.memAlloc(10);
	private boolean empty;

	public MixedBindings(VertexLayout layout) {
		super(layout);
	}

	public MixedBindings(VertexType... types) {
		this(new VertexLayout(types));
	}

	public MixedBindings put(byte vertex) {
		resizeByteBuffer(1);
		nextData.put(vertex);
		coordCount++;
		empty = false;
		return this;
	}

	public MixedBindings put(short vertex) {
		resizeByteBuffer(2);
		nextData.putShort(vertex);
		coordCount++;
		empty = false;
		return this;
	}

	public MixedBindings put(int vertex) {
		resizeByteBuffer(4);
		nextData.putInt(vertex);
		coordCount++;
		empty = false;
		return this;
	}

	public MixedBindings put(float vertex) {
		resizeByteBuffer(4);
		nextData.putFloat(vertex);
		coordCount++;
		empty = false;
		return this;
	}

	private void resizeByteBuffer(int size) {
		while (nextData.position() + size > nextData.capacity()) {
			int newCapacity = nextData.capacity();
			newCapacity += newCapacity >> 1;
			nextData = MemoryUtil.memRealloc(nextData, newCapacity);
		}
	}

	@Override
	protected void uploadVertices(boolean dynamic) {
		layout.byteBufferData(vbo, nextData, dynamic);
	}

	@Override
	public void clear() {
		super.clear();
		if (nextData.capacity() > 256) {
			MemoryUtil.memFree(nextData);
			nextData = MemoryUtil.memAlloc(10);
		} else {
			nextData.clear();
		}
		empty = true;
	}

	@Override
	public void unload() {
		super.unload();
		MemoryUtil.memFree(nextData);
		nextData = null;
	}

	public boolean isEmpty() {
		return empty;
	}
}
