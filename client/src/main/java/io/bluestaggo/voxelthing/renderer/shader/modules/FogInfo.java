package io.bluestaggo.voxelthing.renderer.shader.modules;

import io.bluestaggo.voxelthing.renderer.shader.Shader;
import io.bluestaggo.voxelthing.renderer.shader.uniform.ShaderUniform;
import org.joml.Vector3f;

public class FogInfo {
	public final ShaderUniform<Integer> skyTex;
	public final ShaderUniform<Float> skyWidth;
	public final ShaderUniform<Float> skyHeight;
	public final ShaderUniform<Vector3f> camPos;
	public final ShaderUniform<Float> distHor;
	public final ShaderUniform<Float> distVer;

	public FogInfo(Shader shader) {
		(skyTex = shader.getUniform1i("fogInfo.skyTex")).set(1);
		skyWidth = shader.getUniform1f("fogInfo.skyWidth");
		skyHeight = shader.getUniform1f("fogInfo.skyHeight");
		camPos = shader.getUniform3f("fogInfo.camPos");
		distHor = shader.getUniform1f("fogInfo.distHor");
		distVer = shader.getUniform1f("fogInfo.distVer");
	}

	public void setup(float skyWidth, float skyHeight, Vector3f camPos, float distHor, float distVer) {
		this.skyWidth.set(skyWidth);
		this.skyHeight.set(skyHeight);
		this.camPos.set(camPos);
		this.distHor.set(distHor);
		this.distVer.set(distVer);
	}
}
