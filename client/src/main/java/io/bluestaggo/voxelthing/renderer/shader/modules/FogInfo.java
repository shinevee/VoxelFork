package io.bluestaggo.voxelthing.renderer.shader.modules;

import io.bluestaggo.voxelthing.renderer.shader.Shader;
import io.bluestaggo.voxelthing.renderer.shader.uniform.Uniform1f;
import io.bluestaggo.voxelthing.renderer.shader.uniform.Uniform1i;
import io.bluestaggo.voxelthing.renderer.shader.uniform.Uniform3f;
import org.joml.Vector3f;

public class FogInfo {
	public final Uniform1i skyTex;
	public final Uniform1f skyWidth;
	public final Uniform1f skyHeight;
	public final Uniform3f camPos;
	public final Uniform1f distHor;
	public final Uniform1f distVer;

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
