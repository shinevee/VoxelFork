package io.bluestaggo.voxelthing.renderer.shader.uniform;

import org.joml.Vector3f;

import static org.lwjgl.opengl.GL33C.glUniform3f;

public class Uniform3f extends UniformBase<Vector3f> {
	public Uniform3f(int handle, CharSequence name) {
		super(handle, name);
	}

	@Override
	public void set(Vector3f value) {
		glUniform3f(location, value.x, value.y, value.z);
	}
}
