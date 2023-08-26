package io.bluestaggo.voxelthing.world.entity;

import io.bluestaggo.voxelthing.math.AABB;
import io.bluestaggo.voxelthing.math.MathUtil;
import io.bluestaggo.voxelthing.world.World;

import java.util.List;

public class Entity {
	public final World world;

	protected String texture;

	private final AABB collisionBox = new AABB();
	private final AABB offsetBox = new AABB();
	public double width = 0.4;
	public double height = 1.8;

	public double prevPosX;
	public double prevPosY;
	public double prevPosZ;
	public double posX;
	public double posY;
	public double posZ;
	public double velX;
	public double velY;
	public double velZ;

	public double prevRotPitch;
	public double prevRotYaw;
	public double rotPitch;
	public double rotYaw;

	public boolean onGround;
	public boolean noClip;

	public Entity(World world) {
		this.world = world;
		texture = "/assets/entities/template.png";
		posY = 64.0;
	}

	public void tick() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		prevRotYaw = rotYaw;
		prevRotPitch = rotPitch;

		update();
		updateMovement();
	}

	protected void update() {
		if (velY > -2.0) {
			velY -= 0.1;
		}
	}

	private void updateCollisionBox() {
		collisionBox.setBounds(posX - width, posY, posZ - width, posX + width, posY + height, posZ + width, null);
	}

	private void updateMovement() {
		if (!noClip) {
			updateCollisionBox();
			List<AABB> intersectingBoxes = world.getSurroundingCollision(collisionBox.expandToPoint(velX, velY, velZ, offsetBox));

			double oldVelY = velY;

			for (AABB box : intersectingBoxes) {
				velY = box.calcYOffset(collisionBox, velY);
			}
			collisionBox.offset(0.0, velY, 0.0, collisionBox);

			for (AABB box : intersectingBoxes) {
				velX = box.calcXOffset(collisionBox, velX);
			}
			collisionBox.offset(velX, 0.0, 0.0, collisionBox);

			for (AABB box : intersectingBoxes) {
				velZ = box.calcZOffset(collisionBox, velZ);
			}
			collisionBox.offset(0.0, 0.0, velZ, collisionBox);

			onGround = oldVelY < 0.0 && oldVelY < velY;
		} else {
			onGround = true;
		}

		posX += velX;
		posY += velY;
		posZ += velZ;
		updateCollisionBox();
	}

	public double getPartialX() {
		return MathUtil.lerp(prevPosX, posX, world.partialTick);
	}

	public double getPartialY() {
		return MathUtil.lerp(prevPosY, posY, world.partialTick);
	}

	public double getPartialZ() {
		return MathUtil.lerp(prevPosZ, posZ, world.partialTick);
	}

	public float getRenderX() {
		return (float) getPartialX();
	}

	public float getRenderY() {
		return (float) getPartialY();
	}

	public float getRenderZ() {
		return (float) getPartialZ();
	}

	public float getRenderYaw() {
		return (float) MathUtil.lerp(prevRotYaw, rotYaw, world.partialTick);
	}

	public float getRenderPitch() {
		return (float) MathUtil.lerp(prevRotPitch, rotPitch, world.partialTick);
	}

	public float getRenderWidth() {
		return 2.0f;
	}

	public float getRenderHeight() {
		return 2.0f;
	}

	public int getRenderFrame() {
		return 0;
	}

	public int getRenderRotation(float cameraAngle) {
		return (int) (MathUtil.floorMod(getRenderYaw() - 180.0f - cameraAngle + 22.5f, 360.0f) / 45.0f) % 8;
	}

	public String getTexture() {
		return texture;
	}
}
