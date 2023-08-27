package io.bluestaggo.voxelthing.world.block.texture;

import io.bluestaggo.voxelthing.world.Direction;
import io.bluestaggo.voxelthing.world.IBlockAccess;
import org.joml.Vector2i;

public class AllSidesTexture implements BlockTexture {
	protected final Vector2i vec;

	public AllSidesTexture(int x, int y) {
		vec = new Vector2i(x, y);
	}

	@Override
	public Vector2i get(Direction face, IBlockAccess blockAccess, int x, int y, int z) {
		return vec;
	}
}
