package io.bluestaggo.voxelthing.world.block.texture;

import io.bluestaggo.voxelthing.world.Direction;
import io.bluestaggo.voxelthing.world.IBlockAccess;
import org.joml.Vector2i;

public class SideTopBottomTexture implements IBlockTexture {
	protected final Vector2i side;
	protected final Vector2i top;
	protected final Vector2i bottom;

	public SideTopBottomTexture(int sx, int sy, int tx, int ty, int bx, int by) {
		side = new Vector2i(sx, sy);
		top = new Vector2i(tx, ty);
		bottom = new Vector2i(bx, by);
	}

	@Override
	public Vector2i getTexture(Direction face, IBlockAccess blockAccess, int x, int y, int z) {
		return switch (face) {
			case TOP -> top;
			case BOTTOM -> bottom;
			default -> side;
		};
	}
}
