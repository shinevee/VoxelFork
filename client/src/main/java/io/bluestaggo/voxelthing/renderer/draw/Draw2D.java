package io.bluestaggo.voxelthing.renderer.draw;

import io.bluestaggo.voxelthing.renderer.MainRenderer;
import io.bluestaggo.voxelthing.renderer.screen.ScreenVertex;
import io.bluestaggo.voxelthing.renderer.shader.QuadShader;
import io.bluestaggo.voxelthing.renderer.vertices.Bindings;
import io.bluestaggo.voxelthing.renderer.vertices.FloatBindings;
import io.bluestaggo.voxelthing.renderer.vertices.FloatVertexType;

import java.io.IOException;

public class Draw2D {
	private final MainRenderer renderer;

	private final Bindings<ScreenVertex> bindings = new Bindings<>(ScreenVertex.LAYOUT);
	private final FloatBindings quad = new FloatBindings(FloatVertexType.VECTOR2F);

	private final QuadShader quadShader;

	public Draw2D(MainRenderer renderer) {
		this.renderer = renderer;

		try {
			quadShader = new QuadShader();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		quad.addVertices(1.0f, 1.0f);
		quad.addVertices(1.0f, 0.0f);
		quad.addVertices(0.0f, 0.0f);
		quad.addVertices(0.0f, 1.0f);
		quad.addIndices(0, 1, 2, 2, 3, 0);
		quad.upload(false);
	}

	public void setTextureEnabled(boolean enabled) {
		renderer.screenShader.hasTex.set(enabled);
	}

	public void addVertex(ScreenVertex vertex) {
		bindings.addVertex(vertex);
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
