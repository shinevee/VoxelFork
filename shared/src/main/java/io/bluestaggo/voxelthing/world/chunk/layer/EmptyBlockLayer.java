package io.bluestaggo.voxelthing.world.chunk.layer;

public class EmptyBlockLayer extends BlockLayer {
	private static final byte[] ZERO_BYTE_ARRAY = new byte[0];

	public EmptyBlockLayer() {
	}

	public EmptyBlockLayer(byte[] bytes) {
	}

	@Override
	public int getBlockId(int x, int z) {
		return 0;
	}

	@Override
	public void setBlockId(int x, int z, int id) {
	}

	@Override
	protected int getMaxPaletteSize() {
		return 0;
	}

	@Override
	public byte[] getRawData() {
		return ZERO_BYTE_ARRAY;
	}

	@Override
	public BlockLayer expand() {
		return new NibbleBlockLayer();
	}
}
