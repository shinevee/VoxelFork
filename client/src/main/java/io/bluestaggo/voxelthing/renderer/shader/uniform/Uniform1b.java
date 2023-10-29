package io.bluestaggo.voxelthing.renderer.shader.uniform;

import static org.lwjgl.opengl.GL33C.glUniform1i;

public class Uniform1b extends UniformBase<Boolean> {
	public Uniform1b(int handle, CharSequence name) {
		super(handle, name);
	}

	@Override
	public void set(Boolean value) {
		set(value.booleanValue());
	}

	public void set(boolean value) {
		glUniform1i(location, value ? 1 : 0);
	}
}
