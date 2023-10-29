package io.bluestaggo.voxelthing.renderer.shader;

import io.bluestaggo.voxelthing.renderer.shader.uniform.Uniform4f;
import io.bluestaggo.voxelthing.renderer.shader.uniform.UniformMatrix4fv;

import java.io.IOException;

public class SkyShader extends Shader {
	public final UniformMatrix4fv view;
	public final UniformMatrix4fv proj;

	public final Uniform4f fogCol;
	public final Uniform4f skyCol;

	public SkyShader() throws IOException {
		super("/assets/shaders/sky");
		use();

		view = getUniformMatrix4fv("view");
		proj = getUniformMatrix4fv("proj");

		fogCol = getUniform4f("fogCol");
		skyCol = getUniform4f("skyCol");

		stop();
	}
}
