package io.bluestaggo.voxelthing.renderer.shader;

import io.bluestaggo.voxelthing.renderer.shader.uniform.ShaderUniform;
import org.joml.Matrix4f;

import java.io.IOException;

public class ScreenShader extends Shader {
	public final ShaderUniform<Matrix4f> mvp;

	public final ShaderUniform<Integer> tex;
	public final ShaderUniform<Boolean> hasTex;

	public ScreenShader() throws IOException {
		super("/assets/shaders/screen");
		use();

		mvp = getUniformMatrix4fv("mvp");

		(tex = getUniform1i("tex")).set(0);
		hasTex = getUniform1b("hasTex");

		stop();
	}
}
