package io.bluestaggo.voxelthing.assets;

import org.lwjgl.system.MemoryUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class TextureManager {
	public static final int MAX_TEXTURE_SIZE = 512;

	private final Map<String, Texture> textures = new HashMap<>();
	private final Map<String, Texture> mipmappedTextures = new HashMap<>();
	private final ByteBuffer textureBuffer = MemoryUtil.memAlloc(MAX_TEXTURE_SIZE * MAX_TEXTURE_SIZE * 4);
	private boolean useMipmaps = false;

	private Texture getTextureFromMap(Map<String, Texture> map, String path) {
		if (path == null) {
			return getTextureFromMap(map, "/assets/missing.png");
		}

		if (!textures.containsKey(path)) {
			try {
				Texture texture = loadTexture(path);
				map.put(path, texture);
				return texture;
			} catch (IOException e) {
				if (path.equals("/assets/missing.png")) {
					throw new RuntimeException("Failed to load placeholder texture!");
				}
				return getTextureFromMap(map, "/assets/missing.png");
			}
		}

		return textures.get(path);
	}

	public Texture getTexture(String path) {
		useMipmaps = false;
		return getTextureFromMap(textures, path);
	}

	public Texture getMipmappedTexture(String path) {
		useMipmaps = true;
		return getTextureFromMap(mipmappedTextures, path);
	}

	private Texture loadTexture(String path) throws IOException {
		try (InputStream imageStream = getClass().getResourceAsStream(path)) {
			if (imageStream == null) {
				throw new IOException("Image \"" + path + "\" does not exist!");
			}

			BufferedImage image = ImageIO.read(imageStream);
			int width = image.getWidth();
			int height = image.getHeight();

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
			Texture texture = useMipmaps
					? new MipmappedTexture(textureBuffer, width, height)
					: new Texture(textureBuffer, width, height);

			textureBuffer.clear();
			return texture;
		}
	}

	public void clear() {
		for (Texture texture : textures.values()) {
			texture.unload();
		}
		textures.clear();
	}
}
