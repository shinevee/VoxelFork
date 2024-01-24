package io.bluestaggo.voxelthing.renderer.world;

import io.bluestaggo.voxelthing.math.MathUtil;
import io.bluestaggo.voxelthing.renderer.MainRenderer;
import io.bluestaggo.voxelthing.renderer.vertices.MixedBindings;
import io.bluestaggo.voxelthing.renderer.vertices.VertexLayout;
import io.bluestaggo.voxelthing.window.Window;
import io.bluestaggo.voxelthing.world.ChunkCache;
import io.bluestaggo.voxelthing.world.World;
import io.bluestaggo.voxelthing.world.chunk.Chunk;
import org.joml.Vector3d;

public class ChunkRenderer {
	private final MainRenderer renderer;
	private final World world;
	private final ChunkCache cache;
	private int x, y, z;
	private boolean needsUpdate;
	private boolean empty;
	private boolean emptyTranslucent;
	private boolean chunksLoaded;
	private double firstAppearance;
	private MixedBindings bindings;
	private MixedBindings translucentBindings;

	public ChunkRenderer(MainRenderer renderer, World world, int x, int y, int z) {
		this.renderer = renderer;
		this.world = world;
		this.cache = new ChunkCache(world);
		setPosition(x, y, z);
	}

	public void setPosition(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		needsUpdate = true;
		empty = true;
		emptyTranslucent = true;
		chunksLoaded = false;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public boolean isEmpty() {
		return empty && emptyTranslucent;
	}

	public boolean isEmptyOpaque() {
		return empty;
	}

	public boolean isEmptyTranslucent() {
		return empty;
	}

	public void render() {
		boolean wasEmpty = empty;
		boolean wasEmptyTranslucent = emptyTranslucent;

		if (needsUpdate) {
			empty = true;
			emptyTranslucent = true;
			Chunk chunk = world.getOrLoadChunkAt(x, y, z);

			if (chunk == null || chunk.isEmpty()) {
				needsUpdate = false;
				return;
			}

			if (bindings == null) {
				bindings = new MixedBindings(VertexLayout.WORLD);
			}

			if (translucentBindings == null) {
				translucentBindings = new MixedBindings(VertexLayout.WORLD);
			}

			loadNeighborChunks();
			for (int xx = 0; xx < Chunk.LENGTH; xx++) {
				for (int yy = 0; yy < Chunk.LENGTH; yy++) {
					for (int zz = 0; zz < Chunk.LENGTH; zz++) {
						 renderer.blockRenderer.render(bindings, translucentBindings, cache, chunk, xx, yy, zz);
					}
				}
			}

			empty = bindings.isEmpty();
			emptyTranslucent = translucentBindings.isEmpty();

			if (!empty) {
				bindings.upload(true);
			} else {
				bindings.unload();
				bindings = null;
			}

			if (!emptyTranslucent) {
				translucentBindings.upload(true);
			} else {
				translucentBindings.unload();
				translucentBindings = null;
			}

			if (!chunk.isEmpty() && (wasEmpty && wasEmptyTranslucent)) {
				firstAppearance = Window.getTimeElapsed();
			}

			needsUpdate = false;
		}
	}

	public void draw() {
		if (!empty && bindings != null) {
			bindings.draw();
		}
	}

	public void drawTranslucent() {
		if (!emptyTranslucent && translucentBindings != null) {
			translucentBindings.draw();
		}
	}

	public void loadNeighborChunks() {
		cache.moveTo(x, y, z);
	}

	public void queueUpdate() {
		needsUpdate = true;
	}

	public boolean needsUpdate() {
		return needsUpdate;
	}

	public boolean areChunksLoaded() {
		return chunksLoaded;
	}

	public Chunk getChunk() {
		return world.getChunkAt(x, y, z);
	}

	public boolean inCamera(Camera camera) {
		Vector3d offset = camera.getOffset();
		float minX = (float)(x * Chunk.LENGTH - offset.x);
		float minY = (float)(y * Chunk.LENGTH - offset.y);
		float minZ = (float)(z * Chunk.LENGTH - offset.z);
		float maxX = minX + Chunk.LENGTH;
		float maxY = minY + Chunk.LENGTH;
		float maxZ = minZ + Chunk.LENGTH;
		return camera.getFrustum().testAab(minX, minY, minZ, maxX, maxY, maxZ);
	}

	public double getFadeAmount(double time) {
		return MathUtil.clamp(1.0 - (time - firstAppearance));
	}

	public void unload() {
		if (bindings != null) {
			bindings.unload();
			bindings = null;
		}
	}
}
