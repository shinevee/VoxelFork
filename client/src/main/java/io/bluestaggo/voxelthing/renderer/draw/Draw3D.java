package io.bluestaggo.voxelthing.renderer.draw;

import io.bluestaggo.voxelthing.renderer.GLState;
import io.bluestaggo.voxelthing.renderer.MainRenderer;
import io.bluestaggo.voxelthing.renderer.shader.BillboardShader;
import io.bluestaggo.voxelthing.renderer.vertices.Bindings;
import io.bluestaggo.voxelthing.renderer.vertices.FloatBindings;
import io.bluestaggo.voxelthing.renderer.vertices.FloatVertexType;
import io.bluestaggo.voxelthing.renderer.world.WorldVertex;

import java.io.IOException;

import static org.lwjgl.opengl.GL33C.GL_CULL_FACE;

public class Draw3D {
	private final MainRenderer renderer;

	private final Bindings<WorldVertex> bindings = new Bindings<>(WorldVertex.LAYOUT);
	private final FloatBindings billboard = new FloatBindings(FloatVertexType.VECTOR3F);

	private final BillboardShader billboardShader;

	public Draw3D(MainRenderer renderer) {
		this.renderer = renderer;

		try {
			billboardShader = new BillboardShader();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		billboard.addVertices(1.0f, 1.0f, 0.0f);
		billboard.addVertices(1.0f, 0.0f, 0.0f);
		billboard.addVertices(0.0f, 0.0f, 0.0f);
		billboard.addVertices(0.0f, 1.0f, 0.0f);
		billboard.addIndices(0, 1, 2, 2, 3, 0);
		billboard.upload(false);
	}

	public void addVertex(WorldVertex vertex) {
		bindings.addVertex(vertex);
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
		drawBillboard(billboard, null);
	}

	public void drawBillboard(Billboard billboard, GLState stateParent) {
		try (var state = new GLState(stateParent)) {
			state.disable(GL_CULL_FACE);
			billboardShader.use();
			billboard.applyToShader(billboardShader, renderer.camera.getView());
			this.billboard.draw();
		}
	}

	public void unload() {
		bindings.unload();
		billboard.unload();
	}
}
