package io.bluestaggo.voxelthing.renderer.shader.uniform;

import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL20C.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL33C.glGetUniformLocation;

public class UniformMatrix4fv implements ShaderUniform<Matrix4f> {
	public final int location;

	public UniformMatrix4fv(int handle, CharSequence name) {
		location = glGetUniformLocation(handle, name);
	}

	@Override
	public void set(Matrix4f value) {
		float[] matrix = new float[16];
		glUniformMatrix4fv(location, false, value.get(matrix));
	}
}
