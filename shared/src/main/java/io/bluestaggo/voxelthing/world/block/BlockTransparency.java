package io.bluestaggo.voxelthing.world.block;

public enum BlockTransparency {
	NONE(false, true),
	THICK(true, true),
	FULL(true, false);

	public final boolean transparent;
	public final boolean drawSameFaces;

	BlockTransparency(boolean transparent, boolean drawNeigbors) {
		this.transparent = transparent;
		this.drawSameFaces = drawNeigbors;
	}
}
