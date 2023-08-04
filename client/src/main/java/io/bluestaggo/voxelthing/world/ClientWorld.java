package io.bluestaggo.voxelthing.world;

import io.bluestaggo.voxelthing.Game;
import io.bluestaggo.voxelthing.renderer.world.WorldRenderer;

public class ClientWorld extends World {
	public final Game game;

	public ClientWorld(Game game) {
		super();
		this.game = game;
		game.renderer.worldRenderer.setWorld(this);
	}

	@Override
	public void onBlockUpdate(int x, int y, int z) {
		super.onBlockUpdate(x, y, z);
		WorldRenderer worldRenderer = game.renderer.worldRenderer;
		worldRenderer.markNeighbourUpdateAt(x, y, z);
	}

	@Override
	public void onChunkAdded(int x, int y, int z) {
		super.onChunkAdded(x, y, z);
		WorldRenderer worldRenderer = game.renderer.worldRenderer;
		worldRenderer.markNeighbourChunkUpdateAt(x, y, z);
	}
}
