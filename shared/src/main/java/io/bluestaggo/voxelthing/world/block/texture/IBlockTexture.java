package io.bluestaggo.voxelthing.world.block.texture;

import io.bluestaggo.voxelthing.world.Direction;
import io.bluestaggo.voxelthing.world.IBlockAccess;
import org.joml.Vector2i;

public interface IBlockTexture {
	Vector2i getTexture(Direction face, IBlockAccess blockAccess, int x, int y, int z);
}
