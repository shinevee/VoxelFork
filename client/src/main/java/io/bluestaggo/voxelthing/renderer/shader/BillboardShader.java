package io.bluestaggo.voxelthing.renderer.shader;

import io.bluestaggo.voxelthing.renderer.shader.uniform.ShaderUniform;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.IOException;

public class BillboardShader extends Shader implements BaseFogShader {
	public final ShaderUniform<Matrix4f> modelView;
	public final ShaderUniform<Matrix4f> proj;
	public final ShaderUniform<Vector3f> position;
	public final ShaderUniform<Vector2f> align;
	public final ShaderUniform<Vector2f> size;
	public final ShaderUniform<Vector4f> uvRange;

	public final ShaderUniform<Integer> tex;
	public final ShaderUniform<Boolean> hasTex;
	public final ShaderUniform<Vector4f> color;
	public final ShaderUniform<Integer> skyTex;
	public final ShaderUniform<Float> skyWidth;
	public final ShaderUniform<Float> skyHeight;
	public final ShaderUniform<Vector3f> camPos;
	public final ShaderUniform<Float> camFar;

	public BillboardShader() throws IOException {
		super("/assets/shaders/billboard");
		use();

		modelView = getUniformMatrix4fv("modelView");
		proj = getUniformMatrix4fv("proj");
		position = getUniform3f("position");
		size = getUniform2f("size");
		align = getUniform2f("align");
		uvRange = getUniform4f("uvRange");

		(tex = getUniform1i("tex")).set(0);
		hasTex = getUniform1b("hasTex");
		color = getUniform4f("color");
		(skyTex = getUniform1i("skyTex")).set(1);
		skyWidth = getUniform1f("skyWidth");
		skyHeight = getUniform1f("skyHeight");
		camPos = getUniform3f("camPos");
		camFar = getUniform1f("camFar");

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
