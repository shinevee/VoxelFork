package io.bluestaggo.voxelthing.renderer.shader.uniform;

import org.joml.Vector2f;

import static org.lwjgl.opengl.GL33C.glUniform2f;

public class Uniform2f extends UniformBase<Vector2f> {
	public Uniform2f(int handle, CharSequence name) {
		super(handle, name);
	}

	@Override
	public void set(Vector2f value) {
		glUniform2f(location, value.x, value.y);
	}
}
