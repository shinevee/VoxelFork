package io.bluestaggo.voxelthing.renderer;

import static org.lwjgl.opengl.GL33C.*;

public class Framebuffer {
	private final int handle;
	private final int textureHandle;
	private int width, height;

	public Framebuffer(int width, int height) {
		handle = glGenFramebuffers();
		textureHandle = glGenTextures();
		resize(width, height);

		use();
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, textureHandle, 0);
		int status = glCheckFramebufferStatus(GL_FRAMEBUFFER);
		if (status != GL_FRAMEBUFFER_COMPLETE) {
			System.err.println("Incomplete framebuffer " + handle + ": " + status);
		}
		stop();
	}

	public void resize(int width, int height) {
		if (width != this.width || height != this.height) {
			this.width = width;
			this.height = height;

			glBindTexture(GL_TEXTURE_2D, textureHandle);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, 0);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glBindTexture(GL_TEXTURE_2D, 0);
		}
	}

	public void use() {
		glBindFramebuffer(GL_FRAMEBUFFER, handle);
	}

	public static void stop() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

	public int getTexture() {
		return textureHandle;
	}

	public void unload() {
		glDeleteFramebuffers(handle);
		glDeleteTextures(textureHandle);
	}
}
