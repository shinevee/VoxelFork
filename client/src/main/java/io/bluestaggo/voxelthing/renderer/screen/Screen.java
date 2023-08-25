package io.bluestaggo.voxelthing.renderer.screen;

import io.bluestaggo.voxelthing.window.Window;
import org.joml.Matrix4f;

public class Screen {
	public static final float MAX_DEPTH = 100.0f;

	private final Window window;
	private final Matrix4f viewProj = new Matrix4f();

	private float width = -1;
	private float height = -1;
	public float scale = 1.0f;

	public Screen(Window window) {
		this.window = window;
		updateDimensions();
	}

	public void updateDimensions() {
		float newWidth = window.getWidth() / scale;
		float newHeight = window.getHeight() / scale;

		if (width != newWidth || height != newHeight) {
			width = newWidth;
			height = newHeight;
			viewProj.identity().ortho(0.0f, width, height, 0.0f, 0.0f, MAX_DEPTH);
		}
	}

	public Matrix4f getViewProj() {
		return viewProj;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}
}
