package io.bluestaggo.voxelthing.renderer.draw;

import io.bluestaggo.voxelthing.renderer.MainRenderer;
import io.bluestaggo.voxelthing.renderer.shader.BillboardShader;
import io.bluestaggo.voxelthing.renderer.vertices.FloatBindings;
import io.bluestaggo.voxelthing.renderer.vertices.VertexLayout;
import io.bluestaggo.voxelthing.renderer.vertices.VertexType;

import java.io.IOException;

public class Draw3D {
	private final MainRenderer renderer;

	private final FloatBindings bindings = new FloatBindings(VertexLayout.WORLD);
	private final FloatBindings billboard = new FloatBindings(VertexType.VECTOR3F);
	private final FloatBindings clouds = new FloatBindings(VertexType.VECTOR3F);

	private final BillboardShader billboardShader;

	public Draw3D(MainRenderer renderer) {
		this.renderer = renderer;

		try {
			billboardShader = new BillboardShader();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		billboard.put(1.0f, 1.0f, 0.0f);
		billboard.put(1.0f, 0.0f, 0.0f);
		billboard.put(0.0f, 0.0f, 0.0f);
		billboard.put(0.0f, 1.0f, 0.0f);
		billboard.addIndices(0, 1, 2, 2, 3, 0);
		billboard.upload(false);

		clouds.put(1.0f, 0.0f, 1.0f);
		clouds.put(1.0f, 0.0f, 0.0f);
		clouds.put(0.0f, 0.0f, 0.0f);
		clouds.put(0.0f, 0.0f, 1.0f);
		clouds.addIndices(0, 1, 2, 2, 3, 0);
		clouds.upload(false);
	}

	public void addVertex(float x, float y, float z, float r, float g, float b, float u, float v) {
		bindings.put(x, y, z, r, g, b, u, v);
	}

	public void draw() {
		renderer.worldShader.use();
		bindings.upload(true);
		bindings.draw();
	}

	public void setup() {
		billboardShader.use();
		billboardShader.proj.set(renderer.camera.getProj());
		renderer.setupFogInfo(billboardShader.fogInfo);
	}

	public void drawBillboard(Billboard billboard) {
		billboardShader.use();
		billboard.applyToShader(billboardShader, renderer.camera.getView());
		this.billboard.draw();
	}

	public void unload() {
		bindings.unload();
		billboard.unload();
		billboardShader.unload();
	}
}
