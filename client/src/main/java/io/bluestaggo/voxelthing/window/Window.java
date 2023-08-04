package io.bluestaggo.voxelthing.window;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.stream.Stream;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33C.*;

public class Window {
	public static final int DEFAULT_WIDTH = 640;
	public static final int DEFAULT_HEIGHT = 360;
	public static final boolean LIMIT_FPS = false;

	private final long handle;
	private final GLFWErrorCallback errorCallback;
	private final GLFWFramebufferSizeCallback framebufferSizeCallback;
	private final GLFWCursorPosCallback cursorPosCallback;
	private final GLFWKeyCallback keyCallback;
	private final GLFWMouseButtonCallback mouseButtonCallback;

	private int width = DEFAULT_WIDTH, height = DEFAULT_HEIGHT;
	private double lastMouseX, lastMouseY;
	private double mouseDeltaX, mouseDeltaY;
	private double deltaTime, lastTick;
	private boolean cursorGrabbed;

	private final KeyState[] keyStates;
	private final KeyState[] mouseStates;

	public Window() {
		keyStates = Stream.generate(KeyState::new).limit(GLFW_KEY_LAST + 1).toArray(KeyState[]::new);
		mouseStates = Stream.generate(KeyState::new).limit(GLFW_MOUSE_BUTTON_LAST + 1).toArray(KeyState[]::new);

		errorCallback = GLFWErrorCallback.createPrint(System.err).set();

		if (!glfwInit()) {
			throw new IllegalStateException("Could not initialize GLFW!");
		}

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

		handle = glfwCreateWindow(DEFAULT_WIDTH, DEFAULT_HEIGHT, "Voxel Thing", 0, 0);
		if (handle == 0) {
			throw new IllegalStateException("Failed to open window!");
		}
		glfwMakeContextCurrent(handle);

		GL.createCapabilities();
		glViewport(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
		glfwSwapInterval(LIMIT_FPS ? 1 : 0);

		framebufferSizeCallback = GLFWFramebufferSizeCallback.create(this::onFramebufferSize).set(handle);
		cursorPosCallback = GLFWCursorPosCallback.create(this::onCursorPos).set(handle);
		keyCallback = GLFWKeyCallback.create(this::onKey).set(handle);
		mouseButtonCallback = GLFWMouseButtonCallback.create(this::onMouseButton).set(handle);

		try {
			setIcon("/assets/icon.png");
		} catch (IOException ignored) {
		}

		lastTick = glfwGetTime();
	}

	public void close() {
		glfwSetWindowShouldClose(handle, true);
	}

	public void destroy() {
		glfwTerminate();
		errorCallback.close();
		framebufferSizeCallback.close();
		cursorPosCallback.close();
		keyCallback.close();
		mouseButtonCallback.close();
	}

	private void onFramebufferSize(long handle, int width, int height) {
		glViewport(0, 0, width, height);
		this.width = width;
		this.height = height;
	}

	private void onCursorPos(long handle, double x, double y) {
		mouseDeltaX = x - lastMouseX;
		mouseDeltaY = lastMouseY - y;
		lastMouseX = x;
		lastMouseY = y;
	}

	private void onKey(long handle, int key, int scancode, int action, int mods) {
		if (key < 0 || key >= keyStates.length) return;

		keyStates[key].setPressed(action > 0);
		if (key == GLFW_KEY_ESCAPE) {
			close();
		}
	}

	private void onMouseButton(long handle, int button, int action, int mods) {
		mouseStates[button].setPressed(action > 0);
	}

	public boolean shouldClose() {
		return glfwWindowShouldClose(handle);
	}

	public void update() {
		mouseDeltaX = 0.0D;
		mouseDeltaY = 0.0D;

		for (KeyState keyState : keyStates) {
			keyState.update();
		}
		for (KeyState mouseState : mouseStates) {
			mouseState.update();
		}

		glfwSwapBuffers(handle);
		glfwPollEvents();

		double currentTime = glfwGetTime();
		deltaTime = currentTime - lastTick;
		lastTick = currentTime;

		long freeMB = Runtime.getRuntime().freeMemory() / 1000000L;
		long totalMB = Runtime.getRuntime().totalMemory() / 1000000L;
		long maxMB = Runtime.getRuntime().maxMemory() / 1000000L;

		glfwSetWindowTitle(handle, "Voxel Thing ("
				+ (totalMB - freeMB) + " / " + maxMB + "MB, "
				+ (int)(deltaTime * 1000.0D) + "ms)"
				);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public double getMouseDeltaX() {
		return mouseDeltaX;
	}

	public double getMouseDeltaY() {
		return mouseDeltaY;
	}

	public double getDeltaTime() {
		return deltaTime;
	}

	public void grabCursor() {
		glfwSetInputMode(handle, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		cursorGrabbed = true;
	}

	public void ungrabCursor() {
		glfwSetInputMode(handle, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
		cursorGrabbed = false;
	}

	public void toggleGrabCursor() {
		if (cursorGrabbed) ungrabCursor();
		else grabCursor();
	}

	public boolean isCursorGrabbed() {
		return cursorGrabbed;
	}

	public void setIcon(String path) throws IOException {
		try (InputStream imageStream = getClass().getResourceAsStream(path);
		     GLFWImage image = GLFWImage.malloc();
		     GLFWImage.Buffer imageBuffer = GLFWImage.malloc(1)) {
			if (imageStream == null) {
				throw new IOException("Image \"" + path + "\" does not exist!");
			}

			// Load image
			BufferedImage bufferedImage = ImageIO.read(imageStream);
			int width = bufferedImage.getWidth();
			int height = bufferedImage.getHeight();

			// Load ARGB data
			int[] data = new int[width * height];
			bufferedImage.getRGB(0, 0, width, height, data, 0, width);

			// Convert ARGB to RGBA for OpenGL usage
			try (MemoryStack stack = MemoryStack.stackPush()) {
				ByteBuffer buffer = stack.malloc(width * height * 4);
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						int pixel = data[x + y * width];
						buffer.put((byte) ((pixel >> 16) & 0xFF));
						buffer.put((byte) ((pixel >> 8) & 0xFF));
						buffer.put((byte) (pixel & 0xFF));
						buffer.put((byte) ((pixel >> 24) & 0xFF));
					}
				}
				buffer.flip();

				// Set GLFW image
				image.set(bufferedImage.getWidth(), bufferedImage.getHeight(), buffer);
				imageBuffer.put(image);
				glfwSetWindowIcon(handle, imageBuffer);
			}
		}
	}

	public boolean isKeyDown(int key) {
		return keyStates[key].isPressed();
	}

	public boolean isKeyJustPressed(int key) {
		return keyStates[key].justPressed();
	}

	public boolean isMouseJustPressed(int mouse) {
		return mouseStates[mouse].justPressed();
	}

	public static double getTimeElapsed() {
		return glfwGetTime();
	}
}
