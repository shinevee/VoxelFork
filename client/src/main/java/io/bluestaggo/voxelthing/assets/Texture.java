package io.bluestaggo.voxelthing.assets;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL33C.*;

public class Texture {
	private final int handle;
	public final int width, height;

	public Texture(ByteBuffer data, int width, int height) {
		this.width = width;
		this.height = height;

		handle = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, handle);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, 4);
		glGenerateMipmap(GL_TEXTURE_2D);
		stop();
	}

	public void unload() {
		glDeleteTextures(handle);
	}

	public void use() {
		glBindTexture(GL_TEXTURE_2D, handle);
	}

	public static void stop() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public float uCoord(int x) {
		return x / (float) width;
	}

	public float vCoord(int y) {
		return y / (float) height;
	}
}
