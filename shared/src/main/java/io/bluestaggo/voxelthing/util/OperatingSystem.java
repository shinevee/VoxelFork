package io.bluestaggo.voxelthing.util;

public enum OperatingSystem {
	WINDOWS,
	MACOS,
	LINUX,
	UNKNOWN;

	private static OperatingSystem current;

	public static OperatingSystem get() {
		if (current != null) {
			return current;
		}

		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("win")) {
			current = OperatingSystem.WINDOWS;
		} else if (os.contains("unix") || os.contains("linux")) {
			current = OperatingSystem.LINUX;
		} else if (os.contains("mac")) {
			current = OperatingSystem.MACOS;
		} else {
			current = OperatingSystem.UNKNOWN;
		}

		return current;
	}
}
