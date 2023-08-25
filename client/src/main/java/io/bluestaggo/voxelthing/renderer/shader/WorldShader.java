package io.bluestaggo.voxelthing.renderer.shader;

import io.bluestaggo.voxelthing.renderer.shader.uniform.ShaderUniform;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.IOException;

public class WorldShader extends Shader implements BaseFogShader {
	public final ShaderUniform<Matrix4f> mvp;

	public final ShaderUniform<Integer> tex;
	public final ShaderUniform<Boolean> hasTex;
	public final ShaderUniform<Integer> skyTex;
	public final ShaderUniform<Vector3f> camPos;
	public final ShaderUniform<Float> camFar;
	public final ShaderUniform<Float> skyWidth;
	public final ShaderUniform<Float> skyHeight;
	public final ShaderUniform<Float> fade;

	public WorldShader() throws IOException {
		super("/assets/shaders/world");
		use();

		mvp = getUniformMatrix4fv("mvp");

		(tex = getUniform1i("tex")).set(0);
		hasTex = getUniform1b("hasTex");
		(skyTex = getUniform1i("skyTex")).set(1);
		skyWidth = getUniform1f("skyWidth");
		skyHeight = getUniform1f("skyHeight");
		camPos = getUniform3f("camPos");
		camFar = getUniform1f("camFar");
		fade = getUniform1f("fade");

		stop();
	}

	@Override
	public void setupFog(float skyWidth, float skyHeight, Vector3f camPos, float camFar) {
		this.skyWidth.set(skyWidth);
		this.skyHeight.set(skyHeight);
		this.camPos.set(camPos);
		this.camFar.set(camFar);
	}
}
