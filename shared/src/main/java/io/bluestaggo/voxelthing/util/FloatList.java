package io.bluestaggo.voxelthing.util;

import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.Objects;

public class FloatList {
	private static final int DEFAULT_CAPACITY = 16;
	private static final int LARGE_CAPACITY = 256;
	private static final int SOFT_MAX_ARRAY_LENGTH = Integer.MAX_VALUE - 8;

	private float[] data;
	private int size;

	public FloatList() {
		data = new float[DEFAULT_CAPACITY];
	}

	public FloatList(float... data) {
		this.data = data;
		this.size = data.length;
	}

	public int size() {
		return size;
	}

	public void add(float e) {
		if (size == data.length) {
			data = grow();
		}
		data[size++] = e;
	}

	public void addAll(float... e) {
		while (size + e.length > data.length) {
			data = grow();
		}
		System.arraycopy(e, 0, data, size, e.length);
		size += e.length;
	}

	public float get(int index) {
		Objects.checkIndex(index, size);
		return data[index];
	}

	public void clear() {
		if (data.length >= LARGE_CAPACITY) {
			data = new float[DEFAULT_CAPACITY];
		} else {
			Arrays.fill(data, 0.0f);
		}
		size = 0;
	}

	public float[] toArray() {
		return Arrays.copyOf(data, size);
	}

	public void putToBuffer(FloatBuffer buffer) {
		buffer.put(data, 0, size);
	}

	private float[] grow() {
		int newCapacity;

		int minCapacity = size + 1;
		int oldLength = data.length;
		int minGrowth = minCapacity - oldLength;
		int prefGrowth = oldLength >> 1;
		int prefLength = oldLength + Math.max(minGrowth, prefGrowth); // might overflow
        if (0 < prefLength && prefLength <= SOFT_MAX_ARRAY_LENGTH) {
            newCapacity = prefLength;
        } else {
			int minLength = oldLength + minGrowth;
			if (minLength < 0) {
				throw new OutOfMemoryError(
		                "Required array length " + oldLength + " + " + minGrowth + " is too large");
			} else if (minLength <= SOFT_MAX_ARRAY_LENGTH) {
				newCapacity = SOFT_MAX_ARRAY_LENGTH;
			} else {
				newCapacity = minCapacity;
			}
        }

		return data = Arrays.copyOf(data, newCapacity);
	}
}
