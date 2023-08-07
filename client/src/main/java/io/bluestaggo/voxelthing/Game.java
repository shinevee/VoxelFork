package io.bluestaggo.voxelthing;

import io.bluestaggo.voxelthing.renderer.MainRenderer;
import io.bluestaggo.voxelthing.renderer.shader.Shader;
import io.bluestaggo.voxelthing.window.ClientPlayerController;
import io.bluestaggo.voxelthing.window.Window;
import io.bluestaggo.voxelthing.world.ClientWorld;
import io.bluestaggo.voxelthing.world.World;
import io.bluestaggo.voxelthing.world.entity.IPlayerController;
import io.bluestaggo.voxelthing.world.entity.Player;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33C.glClearColor;

public class Game {
	public static final float TICK_RATE = 1.0f / 20;

	private static Game instance;

	public final Window window;
	public final MainRenderer renderer;

	public World world;
	public Player player;
	public IPlayerController playerController;

	public float mouseSensitivity = 0.25f;

	private double tickTime;

	public Game() {
		instance = this;

		window = new Window();
		window.grabCursor();

		playerController = new ClientPlayerController(this);

		renderer = new MainRenderer(this);

		world = new ClientWorld(this);
		player = new Player(world, playerController);

		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	}

	public static Game getInstance() {
		return instance;
	}

	public void run() {
		while (!window.shouldClose()) {
			update(window.getDeltaTime());
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

	private void update(double delta) {
		tickTime += delta;
		player.onGameUpdate();

		if (tickTime >= TICK_RATE) {
			tickTime %= TICK_RATE;
			player.tick();
		}

		world.partialTick = (tickTime % TICK_RATE) / TICK_RATE;
		renderer.camera.setPosition((float) player.getRenderX(), (float) (player.getRenderY() + player.height - 0.3), (float) player.getRenderZ());
		renderer.camera.setRotation((float) player.rotYaw, (float) player.rotPitch);

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
			int dist = renderer.worldRenderer.renderDistance;
			if (window.isKeyDown(GLFW_KEY_LEFT_SHIFT)) {
				if (dist < 16) {
					dist <<= 1;
				}
			} else if (dist > 1) {
				dist >>= 1;
			}

			renderer.worldRenderer.renderDistance = dist;
			renderer.worldRenderer.loadRenderers();
		}

		if (window.isKeyJustPressed(GLFW_KEY_R)) {
			player.posX = world.random.nextDouble(-1000.0, 1000.0);
			player.posY = 64.0;
			player.posZ = world.random.nextDouble(-1000.0, 1000.0);
			player.velX = 0.0;
			player.velY = 0.0;
			player.velZ = 0.0;
		}

		long freeMB = Runtime.getRuntime().freeMemory() / 1000000L;
		long totalMB = Runtime.getRuntime().totalMemory() / 1000000L;
		long maxMB = Runtime.getRuntime().maxMemory() / 1000000L;

		window.setTitle("Voxel Thing ("
				+ (totalMB - freeMB) + " / " + maxMB + "MB, "
				+ (int)(window.getDeltaTime() * 1000.0D) + "ms)"
				+ " [dist: " + renderer.worldRenderer.renderDistance + "]");
	}

	private void draw() {
		renderer.draw();
	}

	public static void main(String[] args) {
		System.setProperty("java.awt.headless", "true");
		new Game().run();
	}
}
