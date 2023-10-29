package io.bluestaggo.voxelthing.renderer.shader.uniform;

import static org.lwjgl.opengl.GL20C.glGetUniformLocation;

public abstract class UniformBase<T> {
	public final int location;

	public UniformBase(int handle, CharSequence name) {
		location = glGetUniformLocation(handle, name);
	}

	public abstract void set(T value);
}
