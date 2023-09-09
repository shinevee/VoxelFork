package io.bluestaggo.voxelthing.renderer.draw;

import io.bluestaggo.voxelthing.renderer.MainRenderer;
import io.bluestaggo.voxelthing.renderer.shader.BillboardShader;
import io.bluestaggo.voxelthing.renderer.shader.CloudShader;
import io.bluestaggo.voxelthing.renderer.vertices.Bindings;
import io.bluestaggo.voxelthing.renderer.vertices.VertexLayout;
import io.bluestaggo.voxelthing.renderer.vertices.VertexType;

import java.io.IOException;

public class Draw3D {
	private final MainRenderer renderer;

	private final Bindings bindings = new Bindings(VertexLayout.WORLD);
	private final Bindings billboard = new Bindings(VertexType.VECTOR3F);
	private final Bindings clouds = new Bindings(VertexType.VECTOR3F);

	private final BillboardShader billboardShader;
	private final CloudShader cloudShader;

	public Draw3D(MainRenderer renderer) {
		this.renderer = renderer;

		try {
			billboardShader = new BillboardShader();
			cloudShader = new CloudShader();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		billboard.addVertices(1.0f, 1.0f, 0.0f);
		billboard.addVertices(1.0f, 0.0f, 0.0f);
		billboard.addVertices(0.0f, 0.0f, 0.0f);
		billboard.addVertices(0.0f, 1.0f, 0.0f);
		billboard.addIndices(0, 1, 2, 2, 3, 0);
		billboard.upload(false);
		clouds.addVertices(1.0f, 0.0f, 1.0f);
		clouds.addVertices(1.0f, 0.0f, 0.0f);
		clouds.addVertices(0.0f, 0.0f, 0.0f);
		clouds.addVertices(0.0f, 0.0f, 1.0f);
		clouds.addIndices(0, 1, 2, 2, 3, 0);
		clouds.upload(false);
	}

	public void addVertex(float x, float y, float z, float r, float g, float b, float u, float v) {
		bindings.addVertices(x, y, z, r, g, b, u, v);
	}

	public void draw() {
		renderer.worldShader.use();
		bindings.upload(true);
		bindings.draw();
	}

	public void setup() {
		billboardShader.use();
		billboardShader.proj.set(renderer.camera.getProj());
		renderer.setupFogShader(billboardShader);
	}

	public void drawBillboard(Billboard billboard) {
		billboardShader.use();
		billboard.applyToShader(billboardShader, renderer.camera.getView());
		this.billboard.draw();
	}

	public void drawClouds(double ticks) {
		cloudShader.use();
		cloudShader.proj.set(renderer.camera.getProj());
		cloudShader.view.set(renderer.camera.getView());
		cloudShader.camPos.set(renderer.camera.getPosition());
		cloudShader.ticks.set((float)ticks);
		double distance = renderer.camera.getFar()/32;
		if(distance <= 4) {
			cloudShader.fogMultiplier.set(12.0F);
		} else if(distance == 8) {
			cloudShader.fogMultiplier.set(4.5F);
		} else {
			cloudShader.fogMultiplier.set(2.0F);
		}
		renderer.textures.getMipmappedTexture("/assets/environment/clouds.png").use();
		this.clouds.draw();
	}

	public void unload() {
		bindings.unload();
		billboard.unload();
		billboardShader.unload();
	}
}
