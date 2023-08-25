package io.bluestaggo.voxelthing.renderer.shader.uniform;

import static org.lwjgl.opengl.GL20C.glGetUniformLocation;

public abstract class UniformBase<T> implements ShaderUniform<T> {
	public final int location;

	public UniformBase(int handle, CharSequence name) {
		location = glGetUniformLocation(handle, name);
	}
}
