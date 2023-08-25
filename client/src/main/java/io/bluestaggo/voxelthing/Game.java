package io.bluestaggo.voxelthing;

import io.bluestaggo.voxelthing.renderer.MainRenderer;
import io.bluestaggo.voxelthing.window.ClientPlayerController;
import io.bluestaggo.voxelthing.window.Window;
import io.bluestaggo.voxelthing.world.ClientWorld;
import io.bluestaggo.voxelthing.world.World;
import io.bluestaggo.voxelthing.world.entity.IPlayerController;
import io.bluestaggo.voxelthing.world.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33C.glClearColor;

public class Game {
	public static final String VERSION;
	public static final float TICK_RATE = 1.0f / 20;

	static {
		String version = "???";

		try (InputStream stream = Game.class.getResourceAsStream("/version.txt")) {
			if (stream == null) {
				throw new IOException("Failed to get version!");
			}

			var reader = new BufferedReader(new InputStreamReader(stream));
			version = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		VERSION = version;
	}

	private static final String[] SKINS = {
			"joel",
			"staggo",
			"floof",
			"talon"
	};
	private int currentSkin;
	private boolean thirdPerson;
	private boolean debugMenu = true;

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

	private void update(double delta) {
		tickTime += delta;
		player.onGameUpdate();
		player.noClip = window.isKeyDown(GLFW_KEY_Q);

		if (tickTime >= TICK_RATE) {
			tickTime %= TICK_RATE;
			player.tick();
		}

		world.partialTick = (tickTime % TICK_RATE) / TICK_RATE;
		renderer.camera.setPosition((float) player.getRenderX(), (float) (player.getRenderY() + player.height - 0.3), (float) player.getRenderZ());
		renderer.camera.setRotation((float) player.rotYaw, (float) player.rotPitch);

		if (thirdPerson) {
			renderer.camera.moveForward(-4.0f);
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

		if (window.isKeyJustPressed(GLFW_KEY_LEFT_BRACKET) && renderer.screen.scale > 0.0f) {
			renderer.screen.scale -= 0.5f;
		}

		if (window.isKeyJustPressed(GLFW_KEY_RIGHT_BRACKET)) {
			renderer.screen.scale += 0.5f;
		}

		if (window.isKeyJustPressed(GLFW_KEY_F3)) {
			debugMenu = !debugMenu;
		}

		if (window.isKeyJustPressed(GLFW_KEY_F5)) {
			thirdPerson = !thirdPerson;
		}

		if (window.isKeyJustPressed(GLFW_KEY_F6)) {
			if (++currentSkin >= SKINS.length) currentSkin = 0;
		}

		if (window.isMouseJustPressed(GLFW_MOUSE_BUTTON_MIDDLE)) {
			window.toggleGrabCursor();
		}
	}

	private void draw() {
		renderer.draw();
	}

	public String getSkin() {
		return "/assets/entities/" + SKINS[currentSkin] + ".png";
	}

	public boolean showDebug() {
		return debugMenu;
	}

	public static void main(String[] args) {
		System.setProperty("java.awt.headless", "true");
		new Game().run();
	}
}
