package io.bluestaggo.voxelthing.world;

import io.bluestaggo.voxelthing.util.IntList;
import io.bluestaggo.voxelthing.world.block.Block;
import io.bluestaggo.voxelthing.world.storage.ByteBlockStorage;
import io.bluestaggo.voxelthing.world.storage.NibbleBlockStorage;
import io.bluestaggo.voxelthing.world.storage.ShortBlockStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BlockStorage {
	private static final List<Class<? extends BlockStorage>> REGISTERED_TYPES = List.of(
			NibbleBlockStorage.class,
			ByteBlockStorage.class,
			ShortBlockStorage.class
	);

	private final List<Block> mutablePalette;
	private final IntList blockCounts = new IntList();
	public final List<Block> palette;

	public BlockStorage() {
		this(new ArrayList<>());
	}

	public BlockStorage(BlockStorage storage) {
		this(storage.mutablePalette);
	}

	public BlockStorage(List<Block> palette) {
		mutablePalette = palette;
		this.palette = Collections.unmodifiableList(mutablePalette);

		if (palette.size() == 0) {
			palette.add(null);
		}

		for (int i = 0; i < palette.size(); i++) {
			blockCounts.set(i, 0);
		}
	}

	protected abstract int getBlockId(int x, int y, int z);

	public Block getBlock(int x, int y, int z) {
		return mutablePalette.get(getBlockId(x, y, z));
	}

	protected abstract void setBlockId(int x, int y, int z, int id);

	public void setBlock(int x, int y, int z, Block block) {
		int index = mutablePalette.indexOf(block);
		if (index == -1) {
			if (needsExpansion(block)) {
				throw new OutOfMemoryError("Cannot add \"" + block + "\" to palette: ran out of " + getMaxPaletteSize() + "spaces!");
			}

			index = mutablePalette.lastIndexOf(null);
			if (index <= 0) {
				index = mutablePalette.size();
				mutablePalette.add(block);
				blockCounts.add(0);
			} else {
				mutablePalette.set(index, block);
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
					mutablePalette.set(oldId, null);
				}
			}
		}
	}

	protected abstract int getMaxPaletteSize();

	public boolean needsExpansion(Block block) {
		return mutablePalette.size() >= getMaxPaletteSize() && !mutablePalette.contains(block);
	}

	public BlockStorage expand() {
		throw new OutOfMemoryError("Cannot expand to larger block storage!");
	}

	public abstract byte[] getBytes();

	public int getType() {
		return REGISTERED_TYPES.indexOf(getClass());
	}

	protected void updateBlockCounts() {
		for (int i = 0; i < palette.size(); i++) {
			blockCounts.set(i, 0);
		}

		for (int x = 0; x < Chunk.LENGTH; x++) {
			for (int y = 0; y < Chunk.LENGTH; y++) {
				for (int z = 0; z < Chunk.LENGTH; z++) {
					int id = getBlockId(x, y, z);
					if (id > 0) {
						blockCounts.set(id, blockCounts.get(id) + 1);
					}
				}
			}
		}
	}

	public boolean isEmpty() {
		for (int i = 1; i < blockCounts.size(); i++) {
			if (blockCounts.get(i) != 0) {
				return false;
			}
		}

		return true;
	}

	public static BlockStorage decode(byte type, List<Block> palette, byte[] bytes) {
		try {
			return REGISTERED_TYPES.get(type).getDeclaredConstructor(List.class, byte[].class).newInstance(palette, bytes);
		} catch (NoSuchMethodException
				| InstantiationException
				| IllegalAccessException
				| IllegalArgumentException
				| java.lang.reflect.InvocationTargetException e) {
			e.printStackTrace();
			return new ByteBlockStorage(palette, bytes);
		}
	}
}
