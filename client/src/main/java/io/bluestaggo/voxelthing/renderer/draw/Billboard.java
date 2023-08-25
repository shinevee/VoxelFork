package io.bluestaggo.voxelthing.renderer.draw;

import io.bluestaggo.voxelthing.assets.Texture;
import io.bluestaggo.voxelthing.renderer.shader.BillboardShader;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Billboard {
	private final Vector3f position = new Vector3f();
	private final Vector2f size = new Vector2f();
	private final Vector2f align = new Vector2f();
	private final Vector4f color = new Vector4f();
	private final Vector4f uv = new Vector4f();
	private Texture texture;
	private boolean spherical;

	public Billboard() {
		clear();
	}

	public Billboard clear() {
		position.zero();
		size.set(1.0f, 1.0f);
		align.set(0.5f, 0.5f);
		color.set(1.0f, 1.0f, 1.0f, 1.0f);
		uv.set(0.0f, 1.0f, 1.0f, 0.0f);
		return this;
	}

	public Billboard at(float x, float y, float z) {
		position.set(x, y, z);
		return this;
	}

	public Billboard at(Vector3f position) {
		this.position.set(position);
		return this;
	}

	public Billboard scale(float x, float y) {
		size.set(x, y);
		return this;
	}

	public Billboard align(float x, float y) {
		align.set(x, y);
		return this;
	}

	public Billboard withColor(float r, float g, float b) {
		return withColor(r, g, b, 1.0f);
	}

	public Billboard withColor(float r, float g, float b, float a) {
		color.set(r, g, b, a);
		return this;
	}

	public Billboard withUV(float bl, float br, float tl, float tr) {
		uv.set(bl, tr, tl, br);
		return this;
	}

	public Billboard withTexture(Texture texture) {
		this.texture = texture;
		return this;
	}

	public Billboard setSpherical(boolean spherical) {
		this.spherical = spherical;
		return this;
	}

	public void applyToShader(BillboardShader shader, Matrix4f view) {
		if (texture != null) {
			texture.use();
		}

		Matrix4f modelView = view.mul(new Matrix4f().translate(position), new Matrix4f())
				.m00(1.0f).m01(0.0f).m02(0.0f)
				.m20(0.0f).m21(0.0f).m22(1.0f);
		if (spherical) {
			modelView.m10(0.0f).m11(1.0f).m12(0.0f);
		}

		shader.modelView.set(modelView);
		shader.size.set(size);
		shader.align.set(align);
		shader.hasTex.set(texture != null);
		shader.color.set(color);
		shader.uvRange.set(uv);
	}
}
