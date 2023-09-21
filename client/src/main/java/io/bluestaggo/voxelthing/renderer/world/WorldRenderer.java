package io.bluestaggo.voxelthing.renderer.world;

import io.bluestaggo.voxelthing.renderer.GLState;
import io.bluestaggo.voxelthing.renderer.MainRenderer;
import io.bluestaggo.voxelthing.renderer.util.Primitives;
import io.bluestaggo.voxelthing.renderer.vertices.Bindings;
import io.bluestaggo.voxelthing.window.Window;
import io.bluestaggo.voxelthing.world.Chunk;
import io.bluestaggo.voxelthing.world.World;
import org.joml.FrustumIntersection;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL33C.*;

public class WorldRenderer {
	private final MainRenderer renderer;
	private final Bindings background;
	private final Bindings clouds;

	private World world;
	private ChunkRenderer[] chunkRenderers;
	private List<ChunkRenderer> sortedChunkRenderers;
	private int minX, minY, minZ;
	private int maxX, maxY, maxZ;

	public int renderDistanceHor = 16;
	public int renderDistanceVer = 8;
	private int renderRangeHor;
	private int renderRangeVer;

	public WorldRenderer(MainRenderer renderer) {
		this.renderer = renderer;

		background = Primitives.inWorld().generateSphere(null, 1.0f, 16, 16);
		clouds = Primitives.ofVector3f().generatePlane(null);
	}

	public int chunkRendererCoord(int x, int y, int z) {
		x = Math.floorMod(x + renderDistanceHor, renderRangeHor);
		y = Math.floorMod(y + renderDistanceVer, renderRangeVer);
		z = Math.floorMod(z + renderDistanceHor, renderRangeHor);
		return (x * renderRangeHor + z) * renderRangeVer + y;
	}

	public void setWorld(World world) {
		this.world = world;
		this.loadRenderers();
	}

	public void loadRenderers() {
		minX = minZ = -renderDistanceHor;
		maxX = maxZ = renderDistanceHor;
		minY = -renderDistanceVer;
		maxY = renderDistanceVer;
		renderRangeHor = renderDistanceHor * 2 + 1;
		renderRangeVer = renderDistanceVer * 2 + 1;

		if (chunkRenderers != null) {
			for (ChunkRenderer chunkRenderer : chunkRenderers) {
				chunkRenderer.unload();
			}
		}

		chunkRenderers = new ChunkRenderer[renderRangeHor * renderRangeVer * renderRangeHor];
		for (int x = -renderDistanceHor; x <= renderDistanceHor; x++) {
			for (int z = -renderDistanceHor; z <= renderDistanceHor; z++) {
				for (int y = -renderDistanceVer; y <= renderDistanceVer; y++) {
					int i = chunkRendererCoord(x, y, z);
					chunkRenderers[i] = new ChunkRenderer(renderer, world, x, y, z);
				}
			}
		}

		moveRenderers();
	}

	public void draw() {
		int updates = 0;
		int maxUpdates = 1;

		FrustumIntersection frustum = this.renderer.camera.getFrustum();
		double currentTime = Window.getTimeElapsed();

		for (ChunkRenderer chunkRenderer : sortedChunkRenderers) {
			if (!chunkRenderer.inFrustum(frustum)) continue;

			if (updates < maxUpdates) {
				boolean neededUpdate = chunkRenderer.needsUpdate();
				chunkRenderer.render();
				if (neededUpdate) {
					updates++;
				}
			}

			renderer.worldShader.fade.set((float)chunkRenderer.getFadeAmount(currentTime));
			chunkRenderer.draw();
		}

		renderer.worldShader.fade.set(0.0f);
	}

	public void drawSky() {
		try (GLState state = new GLState()) {
			renderer.skyShader.use();
			state.disable(GL_DEPTH_TEST);
			glCullFace(GL_FRONT);
			background.draw();
			glCullFace(GL_BACK);
		}
	}

	public void drawClouds() {
		try (GLState state = new GLState()) {
			renderer.cloudShader.use();
			state.enable(GL_BLEND);
			state.disable(GL_CULL_FACE);
			clouds.draw();
		}
	}

	public void moveRenderers() {
		Vector3f cameraPos = renderer.camera.getPosition();
		int x = (int)Math.floor(cameraPos.x / Chunk.LENGTH);
		int y = (int)Math.floor(cameraPos.y / Chunk.LENGTH);
		int z = (int)Math.floor(cameraPos.z / Chunk.LENGTH);

		minX = x - renderDistanceHor;
		minY = y - renderDistanceVer;
		minZ = z - renderDistanceHor;
		maxX = x + renderDistanceHor;
		maxY = y + renderDistanceVer;
		maxZ = z + renderDistanceHor;

		for (int ax = 0; ax < renderRangeHor; ax++) {
			for (int ay = 0; ay < renderRangeVer; ay++) {
				for (int az = 0; az < renderRangeHor; az++) {
					int cx = ax + x - renderDistanceHor;
					int cy = ay + y - renderDistanceVer;
					int cz = az + z - renderDistanceHor;

					ChunkRenderer renderer = chunkRenderers[chunkRendererCoord(cx, cy, cz)];
					if (renderer.getX() != cx || renderer.getY() != cy || renderer.getZ() != cz) {
						renderer.setPosition(cx, cy, cz);
					}
				}
			}
		}

		sortedChunkRenderers = Arrays.stream(chunkRenderers)
				.sorted(this::compareChunks)
				.toList();
	}

	private int compareChunks(ChunkRenderer a, ChunkRenderer b) {
		Vector3f cameraPos = renderer.camera.getPosition();

		int ax = (a.getX() - (int)(cameraPos.x / Chunk.LENGTH));
		int ay = (a.getY() - (int)(cameraPos.y / Chunk.LENGTH));
		int az = (a.getZ() - (int)(cameraPos.z / Chunk.LENGTH));

		int bx = (b.getX() - (int)(cameraPos.x / Chunk.LENGTH));
		int by = (b.getY() - (int)(cameraPos.y / Chunk.LENGTH));
		int bz = (b.getZ() - (int)(cameraPos.z / Chunk.LENGTH));

		int aDist = ax * ax + ay * ay + az * az;
		int bDist = bx * bx + by * by + bz * bz;
		return Integer.compare(aDist, bDist);
	}

	public void markNeighbourUpdateAt(int x, int y, int z) {
		markUpdateAt(x, y, z);
		markUpdateAt(x - 1, y, z);
		markUpdateAt(x + 1, y, z);
		markUpdateAt(x, y - 1, z);
		markUpdateAt(x, y + 1, z);
		markUpdateAt(x, y, z - 1);
		markUpdateAt(x, y, z + 1);
	}

	private void markUpdateAt(int x, int y, int z) {
		int cx = Math.floorDiv(x, Chunk.LENGTH);
		int cy = Math.floorDiv(y, Chunk.LENGTH);
		int cz = Math.floorDiv(z, Chunk.LENGTH);
		markChunkUpdateAt(cx, cy, cz);
	}

	public void markNeighbourChunkUpdateAt(int x, int y, int z) {
		markChunkUpdateAt(x, y, z);
		markChunkUpdateAt(x - 1, y, z);
		markChunkUpdateAt(x + 1, y, z);
		markChunkUpdateAt(x, y - 1, z);
		markChunkUpdateAt(x, y + 1, z);
		markChunkUpdateAt(x, y, z - 1);
		markChunkUpdateAt(x, y, z + 1);
	}

	private void markChunkUpdateAt(int x, int y, int z) {
		if (x < minX || x > maxX || y < minY || y > maxY || z < minZ || z > maxZ) {
			return;
		}

		ChunkRenderer renderer = chunkRenderers[chunkRendererCoord(x, y, z)];
		if (renderer.getX() == x && renderer.getY() == y && renderer.getZ() == z) {
			renderer.queueUpdate();
		}
	}

	public void unload() {
		background.unload();

		if (chunkRenderers != null) {
			for (ChunkRenderer chunkRenderer : chunkRenderers) {
				chunkRenderer.unload();
			}
		}
	}
}
