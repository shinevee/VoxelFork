package io.bluestaggo.voxelthing.renderer.shader;

import io.bluestaggo.voxelthing.renderer.shader.uniform.ShaderUniform;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.IOException;

public class WorldShader extends Shader {
	public final ShaderUniform<Matrix4f> mvp;

	public final ShaderUniform<Integer> tex;
	public final ShaderUniform<Integer> skyTex;
	public final ShaderUniform<Vector3f> camPos;
	public final ShaderUniform<Float> camFar;
	public final ShaderUniform<Float> width;
	public final ShaderUniform<Float> height;
	public final ShaderUniform<Integer> fogAlgorithm;
	public final ShaderUniform<Float> fade;

	public WorldShader() throws IOException {
		super("/assets/shaders/world");
		use();

		mvp = getUniformMatrix4fv("mvp");

		(tex = getUniform1i("tex")).set(0);
		(skyTex = getUniform1i("skyTex")).set(1);
		camPos = getUniform3f("camPos");
		camFar = getUniform1f("camFar");
		width = getUniform1f("width");
		height = getUniform1f("height");
		fogAlgorithm = getUniform1i("fogAlgorithm");
		fade = getUniform1f("fade");

		stop();
	}
}
