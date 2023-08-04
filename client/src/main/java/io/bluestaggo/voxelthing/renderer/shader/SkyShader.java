package io.bluestaggo.voxelthing.renderer.shader;

import io.bluestaggo.voxelthing.renderer.shader.uniform.ShaderUniform;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.io.IOException;

public class SkyShader extends Shader {
	public final ShaderUniform<Matrix4f> view;
	public final ShaderUniform<Matrix4f> proj;

	public final ShaderUniform<Vector4f> fogCol;
	public final ShaderUniform<Vector4f> skyCol;

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
