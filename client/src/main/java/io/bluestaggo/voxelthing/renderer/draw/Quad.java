package io.bluestaggo.voxelthing.renderer.draw;

import io.bluestaggo.voxelthing.assets.Texture;
import io.bluestaggo.voxelthing.renderer.shader.QuadShader;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class Quad {
	private static final Quad shared = new Quad();
	private final Vector2f position = new Vector2f();
	private final Vector2f size = new Vector2f();
	private final Vector4f color = new Vector4f();
	private final Vector4f uv = new Vector4f();
	private Texture texture;

	public Quad() {
		clear();
	}

	public Quad clear() {
		position.zero();
		size.zero();
		color.set(1.0f, 1.0f, 1.0f, 1.0f);
		uv.set(0.0f, 0.0f, 1.0f, 1.0f);
		texture = null;
		return this;
	}

	public static Quad shared() {
		return shared.clear();
	}

	public Quad at(float x, float y) {
		position.set(x, y);
		return this;
	}

	public Quad at(Vector2f position) {
		this.position.set(position);
		return this;
	}

	public Quad offset(float x, float y) {
		position.add(x, y);
		return this;
	}

	public Quad offset(Vector2f position) {
		this.position.add(position);
		return this;
	}

	public Quad size(float x, float y) {
		size.set(x, y);
		return this;
	}

	public Quad withColor(float r, float g, float b) {
		return withColor(r, g, b, 1.0f);
	}

	public Quad withColor(float r, float g, float b, float a) {
		color.set(r, g, b, a);
		return this;
	}

	public Quad withUV(float bl, float br, float tl, float tr) {
		uv.set(bl, br, tl, tr);
		return this;
	}

	public Quad withTexture(Texture texture) {
		this.texture = texture;
		return this;
	}

	public void applyToShader(QuadShader shader, Matrix4f viewProj) {
		if (texture != null) {
			texture.use();
			if (size.x == 0.0f && size.y == 0.0f) {
				size.set(texture.width, texture.height);
			}
		} else {
			Texture.stop();
		}

		shader.viewProj.set(viewProj);
		shader.offset.set(position);
		shader.size.set(size);
		shader.hasTex.set(texture != null);
		shader.color.set(color);
		shader.uvRange.set(uv);
	}
}
