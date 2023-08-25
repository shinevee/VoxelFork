package io.bluestaggo.voxelthing.renderer.world;

import io.bluestaggo.voxelthing.renderer.vertices.FloatBindings;
import io.bluestaggo.voxelthing.world.Chunk;
import io.bluestaggo.voxelthing.world.Direction;
import io.bluestaggo.voxelthing.world.IBlockAccess;
import io.bluestaggo.voxelthing.world.block.Block;
import org.joml.Vector2i;

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
		void render(FloatBindings bindings, IBlockAccess blockAccess, Block block, int x, int y, int z);
	}

	private float getShade(int amount) {
		return 1.0f - SHADE_FACTOR * amount;
	}

	public boolean render(FloatBindings bindings, IBlockAccess blockAccess, Chunk chunk, int x, int y, int z) {
		Block block = chunk.getBlock(x, y, z);
		if (block == null) {
			return false;
		}

		int xx = x + Chunk.LENGTH * chunk.x;
		int yy = y + Chunk.LENGTH * chunk.y;
		int zz = z + Chunk.LENGTH * chunk.z;
		for (Direction dir : Direction.values()) {
			if (block.isFaceDrawn(blockAccess, xx + dir.X, yy + dir.Y, zz + dir.Z, dir)) {
				SIDE_RENDERERS[dir.ordinal()].render(bindings, blockAccess, block, xx, yy, zz);
			}
		}

		return true;
	}

	private void renderNorthFace(FloatBindings bindings, IBlockAccess blockAccess, Block block, int x, int y, int z) {
		Vector2i texture = block.getTexture().getTexture(Direction.NORTH, blockAccess, x, y, z);
		float texX = texture.x * Block.TEXTURE_WIDTH;
		float texY = texture.y * Block.TEXTURE_WIDTH;
		float texXp = texX + Block.TEXTURE_WIDTH;
		float texYp = texY + Block.TEXTURE_WIDTH;
		float shade = getShade(1);

		bindings.addVertices(   x + 1,  y + 1,  z,  shade,  shade,  shade,  texX,   texY    );
		bindings.addVertices(   x + 1,  y,      z,  shade,  shade,  shade,  texX,   texYp   );
		bindings.addVertices(   x,      y,      z,  shade,  shade,  shade,  texXp,  texYp   );
		bindings.addVertices(   x,      y + 1,  z,  shade,  shade,  shade,  texXp,  texY    );
		bindings.addIndices(0, 1, 2, 2, 3, 0);
	}

	private void renderSouthFace(FloatBindings bindings, IBlockAccess blockAccess, Block block, int x, int y, int z) {
		Vector2i texture = block.getTexture().getTexture(Direction.SOUTH, blockAccess, x, y, z);
		float texX = texture.x * Block.TEXTURE_WIDTH;
		float texY = texture.y * Block.TEXTURE_WIDTH;
		float texXp = texX + Block.TEXTURE_WIDTH;
		float texYp = texY + Block.TEXTURE_WIDTH;
		float shade = getShade(3);

		bindings.addVertices(   x,      y + 1,  z + 1,  shade,  shade,  shade,  texX,   texY    );
		bindings.addVertices(   x,      y,      z + 1,  shade,  shade,  shade,  texX,   texYp   );
		bindings.addVertices(   x + 1,  y,      z + 1,  shade,  shade,  shade,  texXp,  texYp   );
		bindings.addVertices(   x + 1,  y + 1,  z + 1,  shade,  shade,  shade,  texXp,  texY    );
		bindings.addIndices(0, 1, 2, 2, 3, 0);
	}

	private void renderWestFace(FloatBindings bindings, IBlockAccess blockAccess, Block block, int x, int y, int z) {
		Vector2i texture = block.getTexture().getTexture(Direction.WEST, blockAccess, x, y, z);
		float texX = texture.x * Block.TEXTURE_WIDTH;
		float texY = texture.y * Block.TEXTURE_WIDTH;
		float texXp = texX + Block.TEXTURE_WIDTH;
		float texYp = texY + Block.TEXTURE_WIDTH;
		float shade = getShade(2);

		bindings.addVertices(   x,  y + 1,  z,      shade,  shade,  shade,  texX,   texY    );
		bindings.addVertices(   x,  y,      z,      shade,  shade,  shade,  texX,   texYp   );
		bindings.addVertices(   x,  y,      z + 1,  shade,  shade,  shade,  texXp,  texYp   );
		bindings.addVertices(   x,  y + 1,  z + 1,  shade,  shade,  shade,  texXp,  texY    );
		bindings.addIndices(0, 1, 2, 2, 3, 0);
	}

	private void renderEastFace(FloatBindings bindings, IBlockAccess blockAccess, Block block, int x, int y, int z) {
		Vector2i texture = block.getTexture().getTexture(Direction.EAST, blockAccess, x, y, z);
		float texX = texture.x * Block.TEXTURE_WIDTH;
		float texY = texture.y * Block.TEXTURE_WIDTH;
		float texXp = texX + Block.TEXTURE_WIDTH;
		float texYp = texY + Block.TEXTURE_WIDTH;
		float shade = getShade(2);

		bindings.addVertices(   x + 1,  y + 1,  z + 1,  shade,  shade,  shade,  texX,   texY    );
		bindings.addVertices(   x + 1,  y,      z + 1,  shade,  shade,  shade,  texX,   texYp   );
		bindings.addVertices(   x + 1,  y,      z,      shade,  shade,  shade,  texXp,  texYp   );
		bindings.addVertices(   x + 1,  y + 1,  z,      shade,  shade,  shade,  texXp,  texY    );
		bindings.addIndices(0, 1, 2, 2, 3, 0);
	}

	private void renderBottomFace(FloatBindings bindings, IBlockAccess blockAccess, Block block, int x, int y, int z) {
		Vector2i texture = block.getTexture().getTexture(Direction.BOTTOM, blockAccess, x, y, z);
		float texX = texture.x * Block.TEXTURE_WIDTH;
		float texY = texture.y * Block.TEXTURE_WIDTH;
		float texXp = texX + Block.TEXTURE_WIDTH;
		float texYp = texY + Block.TEXTURE_WIDTH;
		float shade = getShade(4);

		bindings.addVertices(   x + 1,  y,  z,      shade,  shade,  shade,  texX,   texY    );
		bindings.addVertices(   x + 1,  y,  z + 1,  shade,  shade,  shade,  texX,   texYp   );
		bindings.addVertices(   x,      y,  z + 1,  shade,  shade,  shade,  texXp,  texYp   );
		bindings.addVertices(   x,      y,  z,      shade,  shade,  shade,  texXp,  texY    );
		bindings.addIndices(0, 1, 2, 2, 3, 0);
	}

	private void renderTopFace(FloatBindings bindings, IBlockAccess blockAccess, Block block, int x, int y, int z) {
		Vector2i texture = block.getTexture().getTexture(Direction.TOP, blockAccess, x, y, z);
		float texX = texture.x * Block.TEXTURE_WIDTH;
		float texY = texture.y * Block.TEXTURE_WIDTH;
		float texXp = texX + Block.TEXTURE_WIDTH;
		float texYp = texY + Block.TEXTURE_WIDTH;
		float shade = getShade(0);

		bindings.addVertices(   x + 1,  y + 1,  z + 1,  shade,  shade,  shade,  texX,   texY    );
		bindings.addVertices(   x + 1,  y + 1,  z,      shade,  shade,  shade,  texX,   texYp   );
		bindings.addVertices(   x,      y + 1,  z,      shade,  shade,  shade,  texXp,  texYp   );
		bindings.addVertices(   x,      y + 1,  z + 1,  shade,  shade,  shade,  texXp,  texY    );
		bindings.addIndices(0, 1, 2, 2, 3, 0);
	}
}
