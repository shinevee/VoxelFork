package io.bluestaggo.voxelthing.gui;

public enum Icon {
	DELETE(0, 0),
	RENAME(1, 0);

	public final int texX, texY;

	Icon(int texX, int texY) {
		this.texX = texX;
		this.texY = texY;
	}
}
