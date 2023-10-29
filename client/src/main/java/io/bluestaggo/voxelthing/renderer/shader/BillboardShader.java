package io.bluestaggo.voxelthing.renderer.shader;

import io.bluestaggo.voxelthing.renderer.shader.modules.FogInfo;
import io.bluestaggo.voxelthing.renderer.shader.uniform.*;

import java.io.IOException;

public class BillboardShader extends Shader {
	public final UniformMatrix4fv modelView;
	public final UniformMatrix4fv proj;
	public final Uniform3f position;
	public final Uniform2f align;
	public final Uniform2f size;
	public final Uniform4f uvRange;

	public final Uniform1i tex;
	public final Uniform1b hasTex;
	public final Uniform4f color;
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
