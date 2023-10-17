package io.bluestaggo.voxelthing.world.chunk.layer;

import java.util.List;

public abstract class BlockLayer {
	private static final List<Class<? extends BlockLayer>> REGISTERED_TYPES = List.of(
			EmptyBlockLayer.class,
			NibbleBlockLayer.class,
			ByteBlockLayer.class,
			ShortBlockLayer.class
	);

	public abstract int getBlockId(int x, int z);

	public abstract void setBlockId(int x, int z, int id);

	protected abstract int getMaxPaletteSize();

	public boolean needsExpansion(int id) {
		return id >= getMaxPaletteSize();
	}

	public BlockLayer expand() {
		throw new OutOfMemoryError("Cannot expand to larger block storage!");
	}

	public abstract byte[] getRawData();

	public byte getType() {
		return (byte) REGISTERED_TYPES.indexOf(getClass());
	}

	public static BlockLayer decode(byte type, byte[] bytes) {
		try {
			return REGISTERED_TYPES.get(type).getDeclaredConstructor(byte[].class).newInstance(bytes);
		} catch (NoSuchMethodException
				| InstantiationException
				| IllegalAccessException
				| IllegalArgumentException
				| java.lang.reflect.InvocationTargetException e) {
			e.printStackTrace();
			return new EmptyBlockLayer();
		}
	}
}
