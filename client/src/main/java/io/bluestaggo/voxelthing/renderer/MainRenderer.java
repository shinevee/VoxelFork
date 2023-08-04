package io.bluestaggo.voxelthing.renderer;

import io.bluestaggo.voxelthing.Game;
import io.bluestaggo.voxelthing.assets.TextureManager;
import io.bluestaggo.voxelthing.renderer.shader.Shader;
import io.bluestaggo.voxelthing.renderer.shader.SkyShader;
import io.bluestaggo.voxelthing.renderer.shader.WorldShader;
import io.bluestaggo.voxelthing.renderer.world.BlockRenderer;
import io.bluestaggo.voxelthing.renderer.world.WorldRenderer;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.io.IOException;

import static org.lwjgl.opengl.GL33C.*;

public class MainRenderer {
	public final TextureManager textures;
	public final WorldShader worldShader;
	public final SkyShader skyShader;

	public final Game game;
	public final Camera camera;

	public final WorldRenderer worldRenderer;
	public final BlockRenderer blockRenderer;

	private final Vector4f fogColor = new Vector4f(0.6f, 0.8f, 1.0f, 1.0f);
	private final Vector4f skyColor = new Vector4f(0.2f, 0.6f, 1.0f, 1.0f);
	private final Framebuffer skyFramebuffer;

	private double updateTick;

	public MainRenderer(Game game) {
		this.game = game;

		try {
			textures = new TextureManager();
			worldShader = new WorldShader();
			skyShader = new SkyShader();

			camera = new Camera(game.window);

			worldRenderer = new WorldRenderer(this);
			blockRenderer = new BlockRenderer();

			skyFramebuffer = new Framebuffer(game.window.getWidth(), game.window.getHeight());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void draw() {
		skyFramebuffer.resize(game.window.getWidth(), game.window.getHeight());

		updateTick += game.window.getDeltaTime();
		if (updateTick >= 1.0D) {
			worldRenderer.moveRenderers();
		}

		camera.setFar(worldRenderer.renderDistance * 32);

		glClearColor(skyColor.x, skyColor.y, skyColor.z, skyColor.w);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		Matrix4f view = camera.getView();
		Matrix4f proj = camera.getProj();
		Matrix4f viewProj = proj.mul(view, new Matrix4f());

		try (var state = new GLState()) {
			state.enable(GL_CULL_FACE);
			state.enable(GL_DEPTH_TEST);
			glCullFace(GL_FRONT);

			setupSkyShader(view, proj);
			textures.useTexture(null);
			skyFramebuffer.use();
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			worldRenderer.drawSky();
			Framebuffer.stop();
			worldRenderer.drawSky();

			setupWorldShader(viewProj);
			textures.useTexture("/assets/blocks.png", 0);
			textures.useTexture(skyFramebuffer.getTexture(), 1);
			worldRenderer.draw();
			glActiveTexture(0);

			Shader.stop();
		}
	}

	private void setupWorldShader(Matrix4f viewProj) {
		worldShader.use();
		worldShader.mvp.set(viewProj);
		worldShader.camPos.set(camera.getPosition());
		worldShader.camFar.set(camera.getFar());
		worldShader.width.set((float)game.window.getWidth());
		worldShader.height.set((float)game.window.getHeight());
	}

	private void setupSkyShader(Matrix4f view, Matrix4f proj) {
		skyShader.use();
		skyShader.view.set(view);
		skyShader.proj.set(proj);
		skyShader.fogCol.set(fogColor);
		skyShader.skyCol.set(skyColor);
	}

	public void unload() {
		skyShader.unload();
		worldShader.unload();
		skyFramebuffer.unload();
	}
}
