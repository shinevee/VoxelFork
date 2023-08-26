package io.bluestaggo.voxelthing.renderer.shader;

import io.bluestaggo.voxelthing.renderer.shader.uniform.ShaderUniform;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.io.IOException;

public class QuadShader extends Shader {
	public final ShaderUniform<Matrix4f> viewProj;
	public final ShaderUniform<Vector2f> offset;
	public final ShaderUniform<Vector2f> size;
	public final ShaderUniform<Vector4f> uvRange;

	public final ShaderUniform<Integer> tex;
	public final ShaderUniform<Boolean> hasTex;
	public final ShaderUniform<Vector4f> color;

	public QuadShader() throws IOException {
		super("/assets/shaders/quad");
		use();

		viewProj = getUniformMatrix4fv("viewProj");
		size = getUniform2f("size");
		offset = getUniform2f("offset");
		uvRange = getUniform4f("uvRange");

		(tex = getUniform1i("tex")).set(0);
		hasTex = getUniform1b("hasTex");
		color = getUniform4f("color");

		stop();
	}
}
