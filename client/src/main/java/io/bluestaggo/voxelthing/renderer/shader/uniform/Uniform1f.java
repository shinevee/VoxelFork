package io.bluestaggo.voxelthing.renderer.shader.uniform;

import static org.lwjgl.opengl.GL33C.glUniform1f;

public class Uniform1f extends UniformBase<Float> {
	public Uniform1f(int handle, CharSequence name) {
		super(handle, name);
	}

	@Override
	public void set(Float value) {
		set(value.floatValue());
	}

	public void set(double value) {
		set((float) value);
	}

	public void set(float value) {
		glUniform1f(location, value);
	}
}
