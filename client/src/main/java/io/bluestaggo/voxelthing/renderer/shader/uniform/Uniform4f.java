package io.bluestaggo.voxelthing.renderer.shader.uniform;

import org.joml.Vector4f;

import static org.lwjgl.opengl.GL33C.glUniform4f;

public class Uniform4f extends UniformBase<Vector4f> {
	public Uniform4f(int handle, CharSequence name) {
		super(handle, name);
	}

	@Override
	public void set(Vector4f value) {
		set(value.x, value.y, value.z, value.w);
	}

	public void set(float x, float y, float z, float w) {
		glUniform4f(location, x, y, z, w);
	}
}
