package io.bluestaggo.voxelthing.renderer.shader;

import io.bluestaggo.voxelthing.renderer.shader.modules.FogInfo;
import io.bluestaggo.voxelthing.renderer.shader.uniform.ShaderUniform;
import org.joml.Matrix4f;

import java.io.IOException;

public class WorldShader extends Shader {
	public final ShaderUniform<Matrix4f> mvp;

	public final ShaderUniform<Integer> tex;
	public final ShaderUniform<Boolean> hasTex;
	public final ShaderUniform<Float> fade;
	public final FogInfo fogInfo;

	public WorldShader() throws IOException {
		super("/assets/shaders/world");
		use();

		mvp = getUniformMatrix4fv("mvp");

		(tex = getUniform1i("tex")).set(0);
		hasTex = getUniform1b("hasTex");
		fade = getUniform1f("fade");
		fogInfo = new FogInfo(this);

		stop();
	}
}
