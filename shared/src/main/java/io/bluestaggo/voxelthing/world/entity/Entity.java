package io.bluestaggo.voxelthing.world.entity;

import io.bluestaggo.voxelthing.math.AABB;
import io.bluestaggo.voxelthing.math.MathUtil;
import io.bluestaggo.voxelthing.world.World;

import java.util.List;

public class Entity {
	public final World world;

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

	public Entity(World world) {
		this.world = world;
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
		updateCollisionBox();
		List<AABB> intersectingBoxes = world.getSurroundingCollision(collisionBox.expandToPoint(velX, velY, velZ, offsetBox));

		double oldVelY = velY;

		for (AABB box : intersectingBoxes) {
			velX = box.calcXOffset(collisionBox, velX);
			velY = box.calcYOffset(collisionBox, velY);
			velZ = box.calcZOffset(collisionBox, velZ);
		}

		onGround = oldVelY < 0.0 && oldVelY < velY;

		posX += velX;
		posY += velY;
		posZ += velZ;
		updateCollisionBox();
	}

	public double getRenderX() {
		return MathUtil.lerp(prevPosX, posX, world.partialTick);
	}

	public double getRenderY() {
		return MathUtil.lerp(prevPosY, posY, world.partialTick);
	}

	public double getRenderZ() {
		return MathUtil.lerp(prevPosZ, posZ, world.partialTick);
	}

	public double getRenderYaw() {
		return MathUtil.lerp(prevRotYaw, rotYaw, world.partialTick);
	}

	public double getRenderPitch() {
		return MathUtil.lerp(prevRotPitch, rotPitch, world.partialTick);
	}
}
