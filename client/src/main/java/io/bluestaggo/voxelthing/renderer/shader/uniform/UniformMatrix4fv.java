package io.bluestaggo.voxelthing.renderer.shader.uniform;

import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL20C.glUniformMatrix4fv;

public class UniformMatrix4fv extends UniformBase<Matrix4f> {
	public UniformMatrix4fv(int handle, CharSequence name) {
		super(handle, name);
	}

	@Override
	public void set(Matrix4f value) {
		float[] matrix = new float[16];
		glUniformMatrix4fv(location, false, value.get(matrix));
	}
}
