package io.bluestaggo.voxelthing.renderer;

import io.bluestaggo.voxelthing.Game;
import io.bluestaggo.voxelthing.assets.FontManager;
import io.bluestaggo.voxelthing.assets.Texture;
import io.bluestaggo.voxelthing.assets.TextureManager;
import io.bluestaggo.voxelthing.renderer.draw.Billboard;
import io.bluestaggo.voxelthing.renderer.draw.Draw2D;
import io.bluestaggo.voxelthing.renderer.draw.Draw3D;
import io.bluestaggo.voxelthing.renderer.draw.Quad;
import io.bluestaggo.voxelthing.renderer.screen.Screen;
import io.bluestaggo.voxelthing.renderer.shader.*;
import io.bluestaggo.voxelthing.renderer.world.BlockRenderer;
import io.bluestaggo.voxelthing.renderer.world.Camera;
import io.bluestaggo.voxelthing.renderer.world.WorldRenderer;
import io.bluestaggo.voxelthing.window.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.IOException;

import static org.lwjgl.opengl.GL33C.*;

public class MainRenderer {
	public final Game game;
	public final Camera camera;
	public final Screen screen;

	public final TextureManager textures;
	public final FontManager fonts;

	public final WorldShader worldShader;
	public final SkyShader skyShader;
	public final ScreenShader screenShader;

	public final Draw3D draw3D;
	public final WorldRenderer worldRenderer;
	public final BlockRenderer blockRenderer;

	public final Draw2D draw2D;

	private final Vector4f fogColor = new Vector4f(0.6f, 0.8f, 1.0f, 1.0f);
	private final Vector4f skyColor = new Vector4f(0.2f, 0.6f, 1.0f, 1.0f);
	private final Framebuffer skyFramebuffer;

	private final Vector3f prevUpdatePos = new Vector3f();

	public MainRenderer(Game game) {
		this.game = game;

		try {
			camera = new Camera(game.window);
			camera.getPosition(prevUpdatePos);
			screen = new Screen(game.window);

			textures = new TextureManager();
			fonts = new FontManager(this);

			worldShader = new WorldShader();
			skyShader = new SkyShader();
			screenShader = new ScreenShader();

			draw3D = new Draw3D(this);
			worldRenderer = new WorldRenderer(this);
			blockRenderer = new BlockRenderer();

			draw2D = new Draw2D(this);

			skyFramebuffer = new Framebuffer(game.window.getWidth(), game.window.getHeight());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void draw() {
		screen.updateDimensions();
		skyFramebuffer.resize(game.window.getWidth(), game.window.getHeight());

		if (prevUpdatePos.distance(camera.getPosition()) > 8.0f) {
			worldRenderer.moveRenderers();
			camera.getPosition(prevUpdatePos);
		}

		camera.setFar(worldRenderer.renderDistance * 32);

		glClearColor(skyColor.x, skyColor.y, skyColor.z, skyColor.w);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		Matrix4f view = camera.getView();
		Matrix4f proj = camera.getProj();
		Matrix4f viewProj = proj.mul(view, new Matrix4f());
		draw3D.setup();

		try (var state = new GLState()) {
			state.enable(GL_CULL_FACE);
			state.enable(GL_DEPTH_TEST);
			glCullFace(GL_FRONT);

			setupSkyShader(view, proj);
			Texture.stop();
			skyFramebuffer.use();
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			worldRenderer.drawSky();
			Framebuffer.stop();
			worldRenderer.drawSky();

			setupWorldShader(viewProj);
			textures.getWorldTexture("/assets/blocks.png").use();
			glActiveTexture(GL_TEXTURE1);
			glBindTexture(GL_TEXTURE_2D, skyFramebuffer.getTexture());
			glActiveTexture(GL_TEXTURE0);
			worldRenderer.draw();

			String skin = game.getSkin();
			Texture skinTex = textures.getTexture(skin);
			int frame = skin.contains("floof") || skin.contains("talon") ? (int) (Window.getTimeElapsed() * 8.0D) % 8
					: skin.contains("staggo") || skin.contains("talon") ? 1 : 0;
			double walk = skin.contains("staggo") || skin.contains("talon") ? Math.sin(Window.getTimeElapsed() * 8.0D) : 0.0;
			float minX = frame < 5 ? skinTex.uCoord(32) : skinTex.uCoord(64);
			float maxX = frame < 5 ? skinTex.uCoord(64) : skinTex.uCoord(32);
			float minY = frame < 5 ? skinTex.vCoord(frame * 32) : skinTex.vCoord((8 - frame) * 32);
			float maxY = minY + skinTex.vCoord(32);

			if (walk > 0.2) {
				minX += skinTex.uCoord(32);
				maxX += skinTex.uCoord(32);
			} else if (walk < -0.2) {
				minX -= skinTex.uCoord(32);
				maxX -= skinTex.uCoord(32);
			}

			Texture.stop();

			try (var billboardState = new GLState(state)) {
				billboardState.disable(GL_CULL_FACE);
				draw3D.drawBillboard(new Billboard()
						.at((float) game.player.getRenderX(), (float) (game.player.getRenderY() + Math.abs(walk / 2.0)), (float) game.player.getRenderZ())
						.scale(2.0f, 2.0f)
						.align(0.5f, 0.0f)
						.setSpherical(false)
						.withTexture(skinTex)
						.withUV(minX, minY, maxX, maxY));
			}

			Texture.stop();
			Shader.stop();

			screenShader.use();
			screenShader.mvp.set(screen.getViewProj());

			state.disable(GL_CULL_FACE);
			state.disable(GL_DEPTH_TEST);

			Quad quad = new Quad();
			draw2D.drawQuad(quad.clear().at(0, screen.getHeight() - 32 - (float) Math.abs(walk / 2.0) * 16.0f)
					.size(32, 32)
					.withTexture(skinTex)
					.withUV(minX, minY, maxX, maxY));

			fonts.outlined.print("§00ffffVOXEL THING    §00ff00" + Game.VERSION, 5, 5, 1.0f, 1.0f, 1.0f);

			if (game.showDebug()) {
				long freeMB = Runtime.getRuntime().freeMemory() / 1000000L;
				long totalMB = Runtime.getRuntime().totalMemory() / 1000000L;
				long maxMB = Runtime.getRuntime().maxMemory() / 1000000L;

				String[] lines = {
						"Speed", (int)(game.window.getDeltaTime() * 1000.0D) + "ms",
						"Memory", (totalMB - freeMB) + " / " + maxMB + " MB",
						"Render Distance", String.valueOf(worldRenderer.renderDistance),
						"GUI Scale", String.valueOf(screen.scale <= 0.0f ? "auto" : screen.scale)
				};

				StringBuilder debugBuilder = new StringBuilder();

				for (int i = 0; i < lines.length / 2; i++) {
					String label = lines[i * 2];
					String value = lines[i * 2 + 1];

					if (!debugBuilder.isEmpty()) {
						debugBuilder.append('\n');
					}

					debugBuilder.append("§ffff7f");
					debugBuilder.append(label);
					debugBuilder.append(": §ffffff");
					debugBuilder.append(value);
				}

				fonts.shadowed.print(debugBuilder.toString(), 5, 15);
			}
		}
	}

	private void setupWorldShader(Matrix4f viewProj) {
		worldShader.use();
		worldShader.mvp.set(viewProj);
		setupFogShader(worldShader);
	}

	private void setupSkyShader(Matrix4f view, Matrix4f proj) {
		skyShader.use();
		skyShader.view.set(view);
		skyShader.proj.set(proj);
		skyShader.fogCol.set(fogColor);
		skyShader.skyCol.set(skyColor);
	}

	public void setupFogShader(BaseFogShader shader) {
		shader.setupFog((float) game.window.getWidth(),
				(float) game.window.getHeight(),
				camera.getPosition(),
				camera.getFar());
	}

	public void unload() {
		textures.clear();

		draw2D.unload();

		draw3D.unload();
		worldRenderer.unload();

		skyShader.unload();
		worldShader.unload();
		skyFramebuffer.unload();
	}
}
