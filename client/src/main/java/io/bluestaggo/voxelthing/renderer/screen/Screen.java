package io.bluestaggo.voxelthing.renderer.screen;

import io.bluestaggo.voxelthing.window.Window;
import org.joml.Matrix4f;

public class Screen {
	public static final float MAX_DEPTH = 100.0f;
	public static final int VIRTUAL_WIDTH = 320;
	public static final int VIRTUAL_HEIGHT = 180;

	private final Window window;
	private final Matrix4f viewProj = new Matrix4f();

	private float width = -1;
	private float height = -1;
	public float scale = 0.0f;

	public Screen(Window window) {
		this.window = window;
		updateDimensions();
	}

	public void updateDimensions() {
		float newWidth = window.getWidth();
		float newHeight = window.getHeight();
		float newScale = scale;

		if (scale <= 0.0f) {
			newScale = 1.0f;
			while (newWidth / (newScale + 1) >= VIRTUAL_WIDTH && newHeight / (newScale + 1) >= VIRTUAL_HEIGHT) {
				newScale++;
			}
		}

		newWidth /= newScale;
		newHeight /= newScale;

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
