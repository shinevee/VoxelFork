package io.bluestaggo.voxelthing.assets;

import io.bluestaggo.voxelthing.renderer.MainRenderer;
import io.bluestaggo.voxelthing.renderer.screen.Font;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FontManager {
	private final MainRenderer renderer;
	private final Map<String, Font> fonts = new HashMap<>();

	public final Font normal;
	public final Font shadowed;
	public final Font outlined;

	public FontManager(MainRenderer renderer) {
		this.renderer = renderer;

		normal = getFont("/assets/gui/fonts/normal.png");
		shadowed = getFont("/assets/gui/fonts/shadowed.png");
		outlined = getFont("/assets/gui/fonts/outlined.png");
	}

	public Font getFont(String path) {
		if (path == null) {
			return getFont("/assets/gui/fonts/font.png");
		}

		if (!fonts.containsKey(path)) {
			try {
				return new Font(renderer, path);
			} catch (IOException e) {
				throw new RuntimeException("Failed to load font \"" + path + "\"!");
			}
		}

		return fonts.get(path);
	}
}
