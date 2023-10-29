package io.bluestaggo.voxelthing.renderer.shader;

import io.bluestaggo.voxelthing.renderer.shader.uniform.Uniform1b;
import io.bluestaggo.voxelthing.renderer.shader.uniform.Uniform1i;
import io.bluestaggo.voxelthing.renderer.shader.uniform.UniformMatrix4fv;

import java.io.IOException;

public class ScreenShader extends Shader {
	public final UniformMatrix4fv mvp;

	public final Uniform1i tex;
	public final Uniform1b hasTex;

	public ScreenShader() throws IOException {
		super("/assets/shaders/screen");
		use();

		mvp = getUniformMatrix4fv("mvp");

		(tex = getUniform1i("tex")).set(0);
		hasTex = getUniform1b("hasTex");

		stop();
	}
}
