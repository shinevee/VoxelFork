package io.bluestaggo.voxelthing.world.block.texture;

import io.bluestaggo.voxelthing.world.Direction;
import io.bluestaggo.voxelthing.world.IBlockAccess;
import org.joml.Vector2i;

public class ColumnTexture implements IBlockTexture {
	protected final Vector2i side;
	protected final Vector2i topBottom;

	public ColumnTexture(int sx, int sy, int tbx, int tby) {
		side = new Vector2i(sx, sy);
		topBottom = new Vector2i(tbx, tby);
	}

	@Override
	public Vector2i get(Direction face, IBlockAccess blockAccess, int x, int y, int z) {
		return switch (face) {
			case TOP, BOTTOM -> topBottom;
			default -> side;
		};
	}
}
