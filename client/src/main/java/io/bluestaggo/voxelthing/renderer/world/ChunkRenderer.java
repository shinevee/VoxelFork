package io.bluestaggo.voxelthing.renderer.world;

import io.bluestaggo.voxelthing.renderer.Bindings;
import io.bluestaggo.voxelthing.renderer.MainRenderer;
import io.bluestaggo.voxelthing.world.Chunk;
import io.bluestaggo.voxelthing.world.World;

public class ChunkRenderer {
	private final MainRenderer renderer;
	private final World world;
	private int x, y, z;
	private boolean needsUpdate;
	private boolean empty;

	private Bindings<WorldVertex> bindings = new Bindings<>(WorldVertex.LAYOUT);

	public ChunkRenderer(MainRenderer renderer, World world, int x, int y, int z) {
		this.renderer = renderer;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		needsUpdate = true;
		empty = true;
	}

	public void setPosition(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		needsUpdate = true;
		empty = true;
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
		return empty;
	}

	public void render() {
		if (needsUpdate) {
			empty = true;
			Chunk chunk = world.getChunkAt(x, y, z);

			if (chunk == null || chunk.isEmpty()) {
				needsUpdate = false;
				return;
			}

			for (int x = 0; x < Chunk.LENGTH; x++) {
				for (int y = 0; y < Chunk.LENGTH; y++) {
					for (int z = 0; z < Chunk.LENGTH; z++) {
						 empty &= !renderer.blockRenderer.render(bindings, world,
								 x + Chunk.LENGTH * chunk.x,
								 y + Chunk.LENGTH * chunk.y,
								 z + Chunk.LENGTH * chunk.z);
					}
				}
			}

			if (!empty) {
				bindings.upload(true);
			}
			needsUpdate = false;
		}
	}

	public void draw() {
		if (!empty) {
			bindings.draw();
		}
	}

	public void queueUpdate() {
		needsUpdate = true;
	}

	public boolean needsUpdate() {
		return needsUpdate;
	}
}
