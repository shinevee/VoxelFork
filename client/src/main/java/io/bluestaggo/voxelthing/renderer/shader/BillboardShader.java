package io.bluestaggo.voxelthing.renderer.shader;

import io.bluestaggo.voxelthing.renderer.shader.modules.FogInfo;
import io.bluestaggo.voxelthing.renderer.shader.uniform.ShaderUniform;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.IOException;

public class BillboardShader extends Shader {
	public final ShaderUniform<Matrix4f> modelView;
	public final ShaderUniform<Matrix4f> proj;
	public final ShaderUniform<Vector3f> position;
	public final ShaderUniform<Vector2f> align;
	public final ShaderUniform<Vector2f> size;
	public final ShaderUniform<Vector4f> uvRange;

	public final ShaderUniform<Integer> tex;
	public final ShaderUniform<Boolean> hasTex;
	public final ShaderUniform<Vector4f> color;
	public final FogInfo fogInfo;

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
		fogInfo = new FogInfo(this);

		stop();
	}
}
