package io.bluestaggo.voxelthing.world;

import io.bluestaggo.voxelthing.util.IntList;
import io.bluestaggo.voxelthing.world.block.Block;

import java.util.ArrayList;
import java.util.List;

public abstract class BlockStorage {
	private final List<Block> palette;
	private final IntList blockCounts = new IntList();

	public BlockStorage() {
		this(new ArrayList<>());
	}

	public BlockStorage(BlockStorage storage) {
		this(storage.palette);
	}

	public BlockStorage(List<Block> palette) {
		this.palette = palette;
		if (palette.size() == 0) {
			palette.add(null);
		}

		if (blockCounts.size() == 0) {
			blockCounts.add(0);
		}
	}

	protected abstract int getBlockId(int x, int y, int z);

	public Block getBlock(int x, int y, int z) {
		return palette.get(getBlockId(x, y, z));
	}

	protected abstract void setBlockId(int x, int y, int z, int id);

	public void setBlock(int x, int y, int z, Block block) {
		int index = palette.indexOf(block);
		if (index == -1) {
			if (needsExpansion(block)) {
				throw new OutOfMemoryError("Cannot add \"" + block + "\" to palette: ran out of " + getMaxPaletteSize() + "spaces!");
			}

			index = palette.lastIndexOf(null);
			if (index <= 0) {
				index = palette.size();
				palette.add(block);
				blockCounts.add(0);
			} else {
				palette.set(index, block);
				blockCounts.set(index, 0);
			}
		}

		int oldId = getBlockId(x, y, z);
		if (index != oldId) {
			setBlockId(x, y, z, index);
			blockCounts.set(index, blockCounts.get(index) + 1);

			if (oldId > 0) {
				blockCounts.set(oldId, blockCounts.get(oldId) - 1);
				if (blockCounts.get(oldId) <= 0) {
					palette.set(oldId, null);
				}
			}
		}
	}

	protected abstract int getMaxPaletteSize();

	public boolean needsExpansion(Block block) {
		return palette.size() >= getMaxPaletteSize() && !palette.contains(block);
	}

	public BlockStorage expand() {
		throw new OutOfMemoryError("Cannot expand to larger block storage!");
	}
}
