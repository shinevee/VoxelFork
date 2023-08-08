package io.bluestaggo.voxelthing.renderer;

import io.bluestaggo.voxelthing.Game;
import io.bluestaggo.voxelthing.assets.Texture;
import io.bluestaggo.voxelthing.assets.TextureManager;
import io.bluestaggo.voxelthing.renderer.draw.Billboard;
import io.bluestaggo.voxelthing.renderer.draw.Draw3D;
import io.bluestaggo.voxelthing.renderer.shader.IFogShader;
import io.bluestaggo.voxelthing.renderer.shader.Shader;
import io.bluestaggo.voxelthing.renderer.shader.SkyShader;
import io.bluestaggo.voxelthing.renderer.shader.WorldShader;
import io.bluestaggo.voxelthing.renderer.world.BlockRenderer;
import io.bluestaggo.voxelthing.renderer.world.WorldRenderer;
import org.joml.Matrix4f;
import org.joml.Vector3f;
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
	public final Draw3D draw3D;

	private final Vector4f fogColor = new Vector4f(0.6f, 0.8f, 1.0f, 1.0f);
	private final Vector4f skyColor = new Vector4f(0.2f, 0.6f, 1.0f, 1.0f);
	private final Framebuffer skyFramebuffer;

	private final Vector3f prevUpdatePos = new Vector3f();

	public MainRenderer(Game game) {
		this.game = game;

		try {
			textures = new TextureManager();
			worldShader = new WorldShader();
			skyShader = new SkyShader();

			camera = new Camera(game.window);
			camera.getPosition(prevUpdatePos);

			worldRenderer = new WorldRenderer(this);
			blockRenderer = new BlockRenderer();
			draw3D = new Draw3D(this);

			skyFramebuffer = new Framebuffer(game.window.getWidth(), game.window.getHeight());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void draw() {
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
			textures.getTexture("/assets/blocks.png").use();
			glActiveTexture(GL_TEXTURE1);
			glBindTexture(GL_TEXTURE_2D, skyFramebuffer.getTexture());
			glActiveTexture(GL_TEXTURE0);
			worldRenderer.draw();

			Texture floof = textures.getTexture(game.getSkin());
			draw3D.drawBillboard(new Billboard()
					.at((float) game.player.getRenderX(), (float) game.player.getRenderY(), (float) game.player.getRenderZ())
					.scale(2.0f, 2.0f)
					.align(0.5f, 0.0f)
					.withTexture(floof)
					.withUV(floof.uCoord(32), floof.vCoord(0), floof.uCoord(64), floof.vCoord(32)), state);

			Shader.stop();
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

	public void setupFogShader(IFogShader shader) {
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
	}
}
