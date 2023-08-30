package io.bluestaggo.voxelthing.util;

import java.util.Arrays;

public enum MultithreadingStrategy {
	OFF,
	SINGLE,
	FULL;

	public static MultithreadingStrategy fromString(String name) {
		return Arrays.stream(values())
				.filter(t -> t.toString().equalsIgnoreCase(name))
				.findAny()
				.orElse(MultithreadingStrategy.OFF);
	}
}
