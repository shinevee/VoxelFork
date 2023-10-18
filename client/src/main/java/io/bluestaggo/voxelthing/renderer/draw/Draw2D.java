package io.bluestaggo.voxelthing.renderer.draw;

import io.bluestaggo.voxelthing.renderer.MainRenderer;
import io.bluestaggo.voxelthing.renderer.shader.QuadShader;
import io.bluestaggo.voxelthing.renderer.vertices.FloatBindings;
import io.bluestaggo.voxelthing.renderer.vertices.VertexLayout;
import io.bluestaggo.voxelthing.renderer.vertices.VertexType;

import java.io.IOException;

public class Draw2D {
	private final MainRenderer renderer;

	private final FloatBindings bindings = new FloatBindings(VertexLayout.SCREEN);
	private final FloatBindings quad = new FloatBindings(VertexType.VECTOR2F);

	private final QuadShader quadShader;

	public Draw2D(MainRenderer renderer) {
		this.renderer = renderer;

		try {
			quadShader = new QuadShader();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		quad.put(1.0f, 1.0f);
		quad.put(1.0f, 0.0f);
		quad.put(0.0f, 0.0f);
		quad.put(0.0f, 1.0f);
		quad.addIndices(0, 1, 2, 2, 3, 0);
		quad.upload(false);
	}

	public void setTextureEnabled(boolean enabled) {
		renderer.screenShader.hasTex.set(enabled);
	}

	public void addVertex(float x, float y, float r, float g, float b, float u, float v) {
		bindings.put(x, y, r, g, b, u, v);
	}

	public void addIndex(int i) {
		bindings.addIndex(i);
	}

	public void addIndices(int... i) {
		bindings.addIndices(i);
	}

	public void setup() {
		quadShader.use();
		quadShader.screenScale.set(renderer.screen.getScale());
	}

	public void draw() {
		renderer.screenShader.use();
		bindings.upload(true);
		bindings.draw();
	}

	public void drawQuad(Quad quad) {
		quadShader.use();
		quad.applyToShader(quadShader, renderer.screen.getViewProj());
		this.quad.draw();
	}

	public void unload() {
		bindings.unload();
		quad.unload();
		quadShader.unload();
	}
}
