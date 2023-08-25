package io.bluestaggo.voxelthing.assets;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL33C.*;

public class MipmappedTexture extends Texture {
	public MipmappedTexture(ByteBuffer data, int width, int height) {
		super(data, width, height);
		use();
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, 4);
		glGenerateMipmap(GL_TEXTURE_2D);
		stop();
	}
}
