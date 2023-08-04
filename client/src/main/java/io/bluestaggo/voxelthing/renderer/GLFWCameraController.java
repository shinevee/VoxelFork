package io.bluestaggo.voxelthing.renderer;

import io.bluestaggo.voxelthing.window.Window;

import static org.lwjgl.glfw.GLFW.*;

public class GLFWCameraController implements CameraController {
	private final Window window;

	public GLFWCameraController(Window window) {
		this.window = window;
	}

	@Override
	public boolean moveForward() {
		return window.isKeyDown(GLFW_KEY_W);
	}

	@Override
	public boolean moveBackward() {
		return window.isKeyDown(GLFW_KEY_S);
	}

	@Override
	public boolean moveLeft() {
		return window.isKeyDown(GLFW_KEY_A);
	}

	@Override
	public boolean moveRight() {
		return window.isKeyDown(GLFW_KEY_D);
	}

	@Override
	public float moveYaw() {
		return window.isCursorGrabbed() ? (float) window.getMouseDeltaX() : 0.0f;
	}

	@Override
	public float movePitch() {
		return window.isCursorGrabbed() ? (float) window.getMouseDeltaY() : 0.0f;
	}

	@Override
	public float getSpeed() {
		return CameraController.super.getSpeed() * (float) window.getDeltaTime()
				* (window.isKeyDown(GLFW_KEY_LEFT_CONTROL) ? 5.0f : 1.0f);
	}
}
