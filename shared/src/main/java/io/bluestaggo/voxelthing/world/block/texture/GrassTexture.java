package io.bluestaggo.voxelthing.world.block.texture;

import io.bluestaggo.voxelthing.world.Direction;
import io.bluestaggo.voxelthing.world.IBlockAccess;
import io.bluestaggo.voxelthing.world.block.Block;
import org.joml.Vector2i;

public class GrassTexture extends SideTopBottomTexture {
	public GrassTexture(int sx, int sy, int tx, int ty, int bx, int by) {
		super(sx, sy, tx, ty, bx, by);
	}

	@Override
	public Vector2i get(Direction face, IBlockAccess blockAccess, int x, int y, int z) {
		return switch (face) {
			case TOP -> top;
			case BOTTOM -> bottom;
			default -> {
				if (blockAccess != null && blockAccess.getBlockId(x + face.X, y - 1, z + face.Z) == Block.GRASS.id) {
					yield top;
				}
				yield side;
			}
		};
	}
}
