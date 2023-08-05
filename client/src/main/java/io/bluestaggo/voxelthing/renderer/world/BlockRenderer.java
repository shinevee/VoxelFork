package io.bluestaggo.voxelthing.renderer.world;

import io.bluestaggo.voxelthing.renderer.Bindings;
import io.bluestaggo.voxelthing.world.Chunk;
import io.bluestaggo.voxelthing.world.Direction;
import io.bluestaggo.voxelthing.world.block.Block;

public class BlockRenderer {
	private static final float SHADE_FACTOR = 0.15f;
	private final SideRenderer[] SIDE_RENDERERS = {
			this::renderNorthFace,
			this::renderSouthFace,
			this::renderWestFace,
			this::renderEastFace,
			this::renderBottomFace,
			this::renderTopFace,
	};

	@FunctionalInterface
	private interface SideRenderer {
		void render(Bindings<WorldVertex> bindings, Block block, int x, int y, int z);
	}

	private float getShade(int amount) {
		return 1.0f - SHADE_FACTOR * amount;
	}

	public boolean render(Bindings<WorldVertex> bindings, Chunk chunk, int x, int y, int z) {
		Block block = chunk.getBlock(x, y, z);
		if (block == null) {
			return false;
		}

		int xx = x + Chunk.LENGTH * chunk.x;
		int yy = y + Chunk.LENGTH * chunk.y;
		int zz = z + Chunk.LENGTH * chunk.z;
		for (Direction dir : Direction.values()) {
			if (block.isFaceDrawn(chunk, x + dir.X, y + dir.Y, z + dir.Z, dir)) {
				SIDE_RENDERERS[dir.ordinal()].render(bindings, block, xx, yy, zz);
			}
		}

		return true;
	}

	private void renderNorthFace(Bindings<WorldVertex> bindings, Block block, int x, int y, int z) {
		float texX = block.getTexX() * Block.TEXTURE_WIDTH;
		float texY = block.getTexY() * Block.TEXTURE_WIDTH;
		float texXp = texX + Block.TEXTURE_WIDTH;
		float texYp = texY + Block.TEXTURE_WIDTH;
		float shade = getShade(1);

		bindings.addVertex(new WorldVertex( x,      y + 1,  z,  shade,  shade,  shade,  texXp,  texY    ));
		bindings.addVertex(new WorldVertex( x,      y,      z,  shade,  shade,  shade,  texXp,  texYp   ));
		bindings.addVertex(new WorldVertex( x + 1,  y,      z,  shade,  shade,  shade,  texX ,  texYp   ));
		bindings.addVertex(new WorldVertex( x + 1,  y + 1,  z,  shade,  shade,  shade,  texX ,  texY    ));
		bindings.addIndices(0, 1, 2, 2, 3, 0);
	}

	private void renderSouthFace(Bindings<WorldVertex> bindings, Block block, int x, int y, int z) {
		float texX = block.getTexX() * Block.TEXTURE_WIDTH;
		float texY = block.getTexY() * Block.TEXTURE_WIDTH;
		float texXp = texX + Block.TEXTURE_WIDTH;
		float texYp = texY + Block.TEXTURE_WIDTH;
		float shade = getShade(3);

		bindings.addVertex(new WorldVertex( x + 1,  y + 1,  z + 1,  shade,  shade,  shade,  texXp,  texY    ));
		bindings.addVertex(new WorldVertex( x + 1,  y,      z + 1,  shade,  shade,  shade,  texXp,  texYp   ));
		bindings.addVertex(new WorldVertex( x,      y,      z + 1,  shade,  shade,  shade,  texX ,  texYp   ));
		bindings.addVertex(new WorldVertex( x,      y + 1,  z + 1,  shade,  shade,  shade,  texX ,  texY    ));
		bindings.addIndices(0, 1, 2, 2, 3, 0);
	}

	private void renderWestFace(Bindings<WorldVertex> bindings, Block block, int x, int y, int z) {
		float texX = block.getTexX() * Block.TEXTURE_WIDTH;
		float texY = block.getTexY() * Block.TEXTURE_WIDTH;
		float texXp = texX + Block.TEXTURE_WIDTH;
		float texYp = texY + Block.TEXTURE_WIDTH;
		float shade = getShade(2);

		bindings.addVertex(new WorldVertex( x,  y + 1,  z + 1,  shade,  shade,  shade,  texXp,  texY    ));
		bindings.addVertex(new WorldVertex( x,  y,      z + 1,  shade,  shade,  shade,  texXp,  texYp   ));
		bindings.addVertex(new WorldVertex( x,  y,      z,      shade,  shade,  shade,  texX ,  texYp   ));
		bindings.addVertex(new WorldVertex( x,  y + 1,  z,      shade,  shade,  shade,  texX ,  texY    ));
		bindings.addIndices(0, 1, 2, 2, 3, 0);
	}

	private void renderEastFace(Bindings<WorldVertex> bindings, Block block, int x, int y, int z) {
		float texX = block.getTexX() * Block.TEXTURE_WIDTH;
		float texY = block.getTexY() * Block.TEXTURE_WIDTH;
		float texXp = texX + Block.TEXTURE_WIDTH;
		float texYp = texY + Block.TEXTURE_WIDTH;
		float shade = getShade(2);

		bindings.addVertex(new WorldVertex( x + 1,  y + 1,  z,      shade,  shade,  shade,  texXp,  texY    ));
		bindings.addVertex(new WorldVertex( x + 1,  y,      z,      shade,  shade,  shade,  texXp,  texYp   ));
		bindings.addVertex(new WorldVertex( x + 1,  y,      z + 1,  shade,  shade,  shade,  texX ,  texYp   ));
		bindings.addVertex(new WorldVertex( x + 1,  y + 1,  z + 1,  shade,  shade,  shade,  texX ,  texY    ));
		bindings.addIndices(0, 1, 2, 2, 3, 0);
	}

	private void renderBottomFace(Bindings<WorldVertex> bindings, Block block, int x, int y, int z) {
		float texX = block.getTexX() * Block.TEXTURE_WIDTH;
		float texY = block.getTexY() * Block.TEXTURE_WIDTH;
		float texXp = texX + Block.TEXTURE_WIDTH;
		float texYp = texY + Block.TEXTURE_WIDTH;
		float shade = getShade(4);

		bindings.addVertex(new WorldVertex( x,      y,  z,      shade,  shade,  shade,  texXp,  texY    ));
		bindings.addVertex(new WorldVertex( x,      y,  z + 1,  shade,  shade,  shade,  texXp,  texYp   ));
		bindings.addVertex(new WorldVertex( x + 1,  y,  z + 1,  shade,  shade,  shade,  texX ,  texYp   ));
		bindings.addVertex(new WorldVertex( x + 1,  y,  z,      shade,  shade,  shade,  texX ,  texY    ));
		bindings.addIndices(0, 1, 2, 2, 3, 0);
	}

	private void renderTopFace(Bindings<WorldVertex> bindings, Block block, int x, int y, int z) {
		float texX = block.getTexX() * Block.TEXTURE_WIDTH;
		float texY = block.getTexY() * Block.TEXTURE_WIDTH;
		float texXp = texX + Block.TEXTURE_WIDTH;
		float texYp = texY + Block.TEXTURE_WIDTH;
		float shade = getShade(0);

		bindings.addVertex(new WorldVertex( x,      y + 1,  z + 1,  shade,  shade,  shade,  texXp,  texY    ));
		bindings.addVertex(new WorldVertex( x,      y + 1,  z,      shade,  shade,  shade,  texXp,  texYp   ));
		bindings.addVertex(new WorldVertex( x + 1,  y + 1,  z,      shade,  shade,  shade,  texX ,  texYp   ));
		bindings.addVertex(new WorldVertex( x + 1,  y + 1,  z + 1,  shade,  shade,  shade,  texX ,  texY    ));
		bindings.addIndices(0, 1, 2, 2, 3, 0);
	}
}
