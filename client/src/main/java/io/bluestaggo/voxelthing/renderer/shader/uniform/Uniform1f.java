package io.bluestaggo.voxelthing.renderer.shader.uniform;

import static org.lwjgl.opengl.GL33C.*;

public class Uniform1f implements ShaderUniform<Float> {
	public final int location;

	public Uniform1f(int handle, CharSequence name) {
		location = glGetUniformLocation(handle, name);
	}

	@Override
	public void set(Float value) {
		glUniform1f(location, value);
	}
}
