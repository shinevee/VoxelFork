package io.bluestaggo.voxelthing.world.block.texture;

import io.bluestaggo.voxelthing.world.Direction;
import io.bluestaggo.voxelthing.world.IBlockAccess;
import org.joml.Vector2i;

public interface BlockTexture {
	Vector2i get(Direction face, IBlockAccess blockAccess, int x, int y, int z);

	default Vector2i get(Direction face) {
		return get(face, null, 0, 0, 0);
	}
}
