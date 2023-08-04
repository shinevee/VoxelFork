package io.bluestaggo.voxelthing.renderer;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL33C.*;

public class GLState implements AutoCloseable {
	private final List<Integer> enabled = new ArrayList<>();
	private final List<Integer> disabled = new ArrayList<>();
	private int depthFunc = 0;

	public void enable(int state) {
		if (disabled.contains(state)) {
			disabled.remove((Integer)state);
		} else {
			enabled.add(state);
		}
		glEnable(state);
	}

	public void disable(int state) {
		if (enabled.contains(state)) {
			enabled.remove((Integer)state);
		} else {
			disabled.add(state);
		}
		glDisable(state);
	}

	public void depthFunc(int func) {
		if (func >= GL_NEVER && func <= GL_ALWAYS) {
			depthFunc = func;
			glDepthFunc(func);
		} else {
			depthFunc = 0;
			glDepthFunc(GL_LESS);
		}
	}

	@Override
	public void close() {
		for (int state : enabled) {
			glDisable(state);
		}

		for (int state : disabled) {
			glEnable(state);
		}

		if (depthFunc > 0) {
			glDepthFunc(GL_LESS);
		}
	}
}
