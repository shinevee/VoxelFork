package io.bluestaggo.voxelthing.renderer.shader;

import org.joml.Vector3f;

public interface BaseFogShader {
	void setupFog(float skyWidth, float skyHeight, Vector3f camPos, float camFar);
}
