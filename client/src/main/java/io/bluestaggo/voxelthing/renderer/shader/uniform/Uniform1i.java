package io.bluestaggo.voxelthing.renderer.shader.uniform;

import static org.lwjgl.opengl.GL33C.*;

public class Uniform1i implements ShaderUniform<Integer> {
	public final int location;

	public Uniform1i(int handle, CharSequence name) {
		location = glGetUniformLocation(handle, name);
	}

	@Override
	public void set(Integer value) {
		glUniform1i(location, value);
	}
}
