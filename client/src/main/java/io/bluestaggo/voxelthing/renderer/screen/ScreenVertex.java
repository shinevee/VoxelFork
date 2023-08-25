package io.bluestaggo.voxelthing.renderer.screen;

import io.bluestaggo.voxelthing.renderer.vertices.FloatVertexLayout;
import io.bluestaggo.voxelthing.renderer.vertices.Normalized;
import io.bluestaggo.voxelthing.renderer.vertices.VertexLayout;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class ScreenVertex {
	public static final VertexLayout<ScreenVertex> LAYOUT = new VertexLayout<>(ScreenVertex.class);
	public static final FloatVertexLayout FLOAT_LAYOUT = new FloatVertexLayout(ScreenVertex.class);

	public Vector2f position;
	public @Normalized Vector3f color;
	public Vector2f uv;

	public ScreenVertex() {
		this(new Vector2f(), new Vector3f(), new Vector2f());
	}

	public ScreenVertex(float x, float y, float r, float g, float b, float u, float v) {
		this(new Vector2f(x, y), new Vector3f(r, g, b), new Vector2f(u, v));
	}

	public ScreenVertex(Vector2f position, Vector3f color, Vector2f uv) {
		this.position = position;
		this.color = color;
		this.uv = uv;
	}
}
