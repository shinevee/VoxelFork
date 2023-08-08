package io.bluestaggo.voxelthing.renderer.shader.uniform;

import org.joml.Vector2f;

import static org.lwjgl.opengl.GL33C.glGetUniformLocation;
import static org.lwjgl.opengl.GL33C.glUniform2f;

public class Uniform2f implements ShaderUniform<Vector2f> {
	public final int location;

	public Uniform2f(int handle, CharSequence name) {
		location = glGetUniformLocation(handle, name);
	}

	@Override
	public void set(Vector2f value) {
		glUniform2f(location, value.x, value.y);
	}
}
