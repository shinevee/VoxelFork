package io.bluestaggo.voxelthing;

import io.bluestaggo.voxelthing.renderer.GLFWCameraController;
import io.bluestaggo.voxelthing.renderer.MainRenderer;
import io.bluestaggo.voxelthing.renderer.shader.Shader;
import io.bluestaggo.voxelthing.window.Window;
import io.bluestaggo.voxelthing.world.ClientWorld;
import io.bluestaggo.voxelthing.world.World;

import static org.lwjgl.opengl.GL33C.*;
import static org.lwjgl.glfw.GLFW.*;

public class Game {
	private static Game instance;

	public final Window window;
	public final MainRenderer renderer;

	public World world;

	public Game() {
		instance = this;

		window = new Window();
		window.grabCursor();

		renderer = new MainRenderer(this);
		renderer.camera.setController(new GLFWCameraController(window));

		world = new ClientWorld(this);

		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	}

	public static Game getInstance() {
		return instance;
	}

	public void run() {
		while (!window.shouldClose()) {
			update();
			draw();
			window.update();
		}

		close();
	}

	private void close() {
		renderer.unload();
		window.destroy();
	}

	private int fogAlgorithm = 1;

	private void update() {
		renderer.camera.update();

		if (window.isMouseJustPressed(GLFW_MOUSE_BUTTON_MIDDLE)) {
			window.toggleGrabCursor();
		}

		if (window.isKeyJustPressed(GLFW_KEY_1)) {
			if (++fogAlgorithm > 3) {
				fogAlgorithm = 0;
			}

			renderer.worldShader.use();
			renderer.worldShader.fogAlgorithm.set(fogAlgorithm);
			Shader.stop();
		}

		if (window.isKeyJustPressed(GLFW_KEY_F)) {
			renderer.worldRenderer.renderDistance += window.isKeyDown(GLFW_KEY_LEFT_SHIFT) ? 1 : -1;
			renderer.worldRenderer.loadRenderers();
		}
	}

	private void draw() {
		renderer.draw();
	}

	public static void main(String[] args) {
		System.setProperty("java.awt.headless", "true");
		new Game().run();
	}
}
