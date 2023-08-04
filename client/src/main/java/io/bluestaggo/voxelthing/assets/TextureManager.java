package io.bluestaggo.voxelthing.assets;

import org.lwjgl.system.MemoryUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL33C.*;

public class TextureManager {
	public static final int MAX_TEXTURE_SIZE = 512;

	private final Map<String, Integer> textures = new HashMap<>();
	private final ByteBuffer textureBuffer = MemoryUtil.memAlloc(MAX_TEXTURE_SIZE * MAX_TEXTURE_SIZE * 4);

	public int getTexture(String path) {
		if (path == null) {
			return 0;
		}

		if (!textures.containsKey(path)) {
			try {
				return loadTexture(path);
			} catch (IOException e) {
				try {
					int texture = loadTexture("/assets/missing.png");
					textures.put(path, texture);
					return texture;
				} catch (IOException e2) {
					throw new RuntimeException(e2);
				}
			}
		}
		return textures.get(path);
	}

	private int loadTexture(String path) throws IOException {
		try (InputStream imageStream = getClass().getResourceAsStream(path)) {
			if (imageStream == null) {
				throw new IOException("Image \"" + path + "\" does not exist!");
			}

			BufferedImage image = ImageIO.read(imageStream);
			int width = image.getWidth();
			int height = image.getHeight();

			// Flip image for OpenGL usage
//			AffineTransform transform = AffineTransform.getScaleInstance(1, -1);
//			transform.translate(0, -height);
//			AffineTransformOp operation = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
//			image = operation.filter(image, null);

			// Load ARGB data
			int[] data = new int[width * height];
			image.getRGB(0, 0, width, height, data, 0, width);

			// Convert ARGB to RGBA for OpenGL usage
			textureBuffer.clear();
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int pixel = data[x + y * width];
					textureBuffer.put((byte) ((pixel >> 16) & 0xFF));
					textureBuffer.put((byte) ((pixel >> 8) & 0xFF));
					textureBuffer.put((byte) (pixel & 0xFF));
					textureBuffer.put((byte) ((pixel >> 24) & 0xFF));
				}
			}
			textureBuffer.flip();

			// Generate OpenGL texture
			int handle = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, handle);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, textureBuffer);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, 4);
			glGenerateMipmap(GL_TEXTURE_2D);

			textures.put(path, handle);
			textureBuffer.clear();
			return handle;
		}
	}

	public void useTexture(String path) {
		useTexture(getTexture(path));
	}

	public void useTexture(String path, int index) {
		useTexture(getTexture(path), index);
	}

	public void useTexture(int texture) {
		glBindTexture(GL_TEXTURE_2D, texture);
	}

	public void useTexture(int texture, int index) {
		glActiveTexture(GL_TEXTURE0 + index);
		glBindTexture(GL_TEXTURE_2D, texture);
	}
}
