package io.bluestaggo.voxelthing.util;

import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Objects;

public class IntList {
	private static final int DEFAULT_CAPACITY = 16;
	private static final int SOFT_MAX_ARRAY_LENGTH = Integer.MAX_VALUE - 8;

	private int[] data;
	private int size;

	public IntList() {
		data = new int[DEFAULT_CAPACITY];
	}

	public IntList(int... data) {
		this.data = data;
		this.size = data.length;
	}

	public int size() {
		return size;
	}

	public void add(int e) {
		if (size == data.length) {
			data = grow();
		}
		data[size++] = e;
	}

	public void addAll(int... e) {
		while (size + e.length > data.length) {
			data = grow();
		}
		System.arraycopy(e, 0, data, size, e.length);
		size += e.length;
	}

	public void set(int i, int e) {
		data[i] = e;
	}

	public int get(int index) {
		Objects.checkIndex(index, size);
		return data[index];
	}

	public void clear() {
		data = new int[DEFAULT_CAPACITY];
		size = 0;
	}

	public int[] toArray() {
		return Arrays.copyOf(data, size);
	}

	public void putToBuffer(IntBuffer buffer) {
		buffer.put(data, 0, size);
	}

	private int[] grow() {
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
