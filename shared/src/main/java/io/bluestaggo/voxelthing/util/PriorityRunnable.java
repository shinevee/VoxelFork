package io.bluestaggo.voxelthing.util;

public class PriorityRunnable implements Comparable<PriorityRunnable>, Runnable {
	private final int priority;
	private final Runnable runnable;

	public PriorityRunnable(int priority, Runnable runnable) {
		this.priority = priority;
		this.runnable = runnable;
	}

	@Override
	public int compareTo(PriorityRunnable o) {
		return Integer.compare(priority, o.priority);
	}

	@Override
	public void run() {
		runnable.run();
	}
}
