package io.bluestaggo.voxelthing.renderer.shader;

import io.bluestaggo.voxelthing.renderer.shader.modules.FogInfo;
import io.bluestaggo.voxelthing.renderer.shader.uniform.Uniform1b;
import io.bluestaggo.voxelthing.renderer.shader.uniform.Uniform1f;
import io.bluestaggo.voxelthing.renderer.shader.uniform.Uniform1i;
import io.bluestaggo.voxelthing.renderer.shader.uniform.UniformMatrix4fv;

import java.io.IOException;

public class WorldShader extends Shader {
	public final UniformMatrix4fv mvp;

	public final Uniform1i tex;
	public final Uniform1b hasTex;
	public final Uniform1f fade;
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
