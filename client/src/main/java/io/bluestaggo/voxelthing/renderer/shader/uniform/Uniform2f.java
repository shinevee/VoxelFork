package io.bluestaggo.voxelthing.renderer.shader.uniform;

import org.joml.Vector2f;

import static org.lwjgl.opengl.GL33C.glUniform2f;

public class Uniform2f extends UniformBase<Vector2f> {
	public Uniform2f(int handle, CharSequence name) {
		super(handle, name);
	}

	@Override
	public void set(Vector2f value) {
		set(value.x, value.y);
	}

	public void set(float x, float y) {
		glUniform2f(location, x, y);
	}
}
