package io.bluestaggo.voxelthing.renderer.world;

import io.bluestaggo.voxelthing.assets.Texture;
import io.bluestaggo.voxelthing.renderer.MainRenderer;
import io.bluestaggo.voxelthing.renderer.draw.Billboard;
import io.bluestaggo.voxelthing.world.entity.Entity;

public class EntityRenderer {
	private final MainRenderer renderer;

	public EntityRenderer(MainRenderer renderer) {
		this.renderer = renderer;
	}

	public void renderEntity(Entity entity) {
		Texture texture = renderer.textures.getTexture(entity.getTexture());

		int rotation = entity.getRenderRotation(renderer.camera.getYaw());
		float minX = texture.uCoord(entity.getRenderFrame() * 32);
		float minY = texture.vCoord(rotation * 32);
		float maxX = minX + texture.uCoord(32);
		float maxY = minY + texture.vCoord(32);

		renderer.draw3D.drawBillboard(Billboard.shared()
				.at(entity.getRenderX(), entity.getRenderY(), entity.getRenderZ())
				.scale(entity.getRenderWidth(), entity.getRenderHeight())
				.align(0.5f, 0.0f)
				.withTexture(texture)
				.withUV(minX, minY, maxX, maxY)
		);
	}
}
