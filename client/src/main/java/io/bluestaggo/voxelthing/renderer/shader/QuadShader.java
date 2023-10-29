package io.bluestaggo.voxelthing.renderer.shader;

import io.bluestaggo.voxelthing.renderer.shader.uniform.*;

import java.io.IOException;

public class QuadShader extends Shader {
	public final UniformMatrix4fv viewProj;
	public final Uniform2f offset;
	public final Uniform2f size;
	public final Uniform4f uvRange;
	public final Uniform1f screenScale;

	public final Uniform1i tex;
	public final Uniform1b hasTex;
	public final Uniform4f color;

	public QuadShader() throws IOException {
		super("/assets/shaders/quad");
		use();

		viewProj = getUniformMatrix4fv("viewProj");
		size = getUniform2f("size");
		offset = getUniform2f("offset");
		uvRange = getUniform4f("uvRange");
		screenScale = getUniform1f("screenScale");

		(tex = getUniform1i("tex")).set(0);
		hasTex = getUniform1b("hasTex");
		color = getUniform4f("color");

		stop();
	}
}
