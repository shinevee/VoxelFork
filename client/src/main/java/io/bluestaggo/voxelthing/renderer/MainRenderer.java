package io.bluestaggo.voxelthing.renderer;

import io.bluestaggo.voxelthing.Game;
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
	public final WorldShader worldShader;
	public final SkyShader skyShader;
	public final ScreenShader screenShader;

	public final WorldRenderer worldRenderer;
	public final BlockRenderer blockRenderer;
	public final Draw2D draw2D;
	public final Draw3D draw3D;

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
			worldShader = new WorldShader();
			skyShader = new SkyShader();
			screenShader = new ScreenShader();

			worldRenderer = new WorldRenderer(this);
			blockRenderer = new BlockRenderer();
			draw2D = new Draw2D(this);
			draw3D = new Draw3D(this);

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

			Texture skin = textures.getTexture(game.getSkin());
			int frame = game.getSkin().contains("floof") ? (int) (Window.getTimeElapsed() * 8.0D) % 8
					: game.getSkin().contains("staggo") ? 1 : 0;
			double walk = game.getSkin().contains("staggo") ? Math.sin(Window.getTimeElapsed() * 8.0D) : 0.0;
			float minX = frame < 5 ? skin.uCoord(32) : skin.uCoord(64);
			float maxX = frame < 5 ? skin.uCoord(64) : skin.uCoord(32);
			float minY = frame < 5 ? skin.vCoord(frame * 32) : skin.vCoord((8 - frame) * 32);
			float maxY = minY + skin.vCoord(32);

			if (walk > 0.2) {
				minX += skin.uCoord(32);
				maxX += skin.uCoord(32);
			} else if (walk < -0.2) {
				minX -= skin.uCoord(32);
				maxX -= skin.uCoord(32);
			}

			Texture.stop();

			try (var billboardState = new GLState(state)) {
				billboardState.disable(GL_CULL_FACE);
				draw3D.drawBillboard(new Billboard()
						.at((float) game.player.getRenderX(), (float) (game.player.getRenderY() + Math.abs(walk / 2.0)), (float) game.player.getRenderZ())
						.scale(2.0f, 2.0f)
						.align(0.5f, 0.0f)
						.setSpherical(false)
						.withTexture(skin)
						.withUV(minX, minY, maxX, maxY));
			}

			Texture.stop();
			Shader.stop();

			state.disable(GL_CULL_FACE);
			state.disable(GL_DEPTH_TEST);

			Quad quad = new Quad();
			draw2D.drawQuad(quad.at(38.0f + (float) game.window.getMouseDeltaX(), 38.0f - (float) game.window.getMouseDeltaY())
					.size(14, 14)
					.withColor(0.0f, 0.0f, 0.0f));
			draw2D.drawQuad(quad.offset(2, 2)
					.size(10, 10)
					.withColor(
							game.window.isMouseDown(1) ? 0.0f : 1.0f,
							game.window.isMouseDown(0) ? 0.0f : 1.0f,
							game.window.isMouseDown(2) ? 1.0f : 0.0f));
			draw2D.drawQuad(quad.clear().at(0, screen.getHeight() - 64 - (float) Math.abs(walk / 2.0) * 32.0f)
					.size(64, 64)
					.withTexture(skin)
					.withUV(minX, minY, maxX, maxY));
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
		worldRenderer.unload();
		skyShader.unload();
		worldShader.unload();
		skyFramebuffer.unload();
		draw3D.unload();
	}
}
