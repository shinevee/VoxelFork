package io.bluestaggo.voxelthing.renderer.shader.uniform;

import org.joml.Vector4f;

import static org.lwjgl.opengl.GL33C.glGetUniformLocation;
import static org.lwjgl.opengl.GL33C.glUniform4f;

public class Uniform4f implements ShaderUniform<Vector4f> {
	public final int location;

	public Uniform4f(int handle, CharSequence name) {
		location = glGetUniformLocation(handle, name);
	}

	@Override
	public void set(Vector4f value) {
		glUniform4f(location, value.x, value.y, value.z, value.w);
	}
}
