package io.bluestaggo.voxelthing.renderer.shader.uniform;

import static org.lwjgl.opengl.GL33C.glUniform1i;

public class Uniform1i extends UniformBase<Integer> {
	public Uniform1i(int handle, CharSequence name) {
		super(handle, name);
	}

	@Override
	public void set(Integer value) {
		set(value.intValue());
	}

	public void set(int value) {
		glUniform1i(location, value);
	}
}
