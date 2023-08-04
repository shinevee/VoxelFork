package io.bluestaggo.voxelthing.renderer.world;

import io.bluestaggo.voxelthing.renderer.Normalized;
import io.bluestaggo.voxelthing.renderer.VertexLayout;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class WorldVertex {
	public static final VertexLayout<WorldVertex> LAYOUT = new VertexLayout<>(WorldVertex.class);

	public Vector3f position;
	public @Normalized Vector3f color;
	public Vector2f uv;

	public WorldVertex() {
		this(new Vector3f(), new Vector3f(), new Vector2f());
	}

	public WorldVertex(float x, float y, float z, float r, float g, float b, float u, float v) {
		this(new Vector3f(x, y, z), new Vector3f(r, g, b), new Vector2f(u, v));
	}

	public WorldVertex(Vector3f position, Vector3f color, Vector2f uv) {
		this.position = position;
		this.color = color;
		this.uv = uv;
	}
}
