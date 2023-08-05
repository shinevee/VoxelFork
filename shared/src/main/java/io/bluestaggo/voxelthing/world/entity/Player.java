package io.bluestaggo.voxelthing.world.entity;

import io.bluestaggo.voxelthing.math.MathUtil;
import io.bluestaggo.voxelthing.world.World;

public class Player extends Entity {
	private final IPlayerController controller;

	public double accel = 0.1;
	public double friction = 0.8;
	public double jumpHeight = 0.5;

	public Player(World world, IPlayerController controller) {
		super(world);
		this.controller = controller;
	}

	public void onGameUpdate() {
		rotYaw += controller.moveYaw();
		rotPitch += controller.movePitch();
		rotPitch = MathUtil.clamp(rotPitch, -89.0f, 89.0f);
	}

	protected void update() {
		super.update();

		double yawRad = Math.toRadians(rotYaw);
		double forward = controller.moveForward();
		double side = controller.moveSide();

		double dist = Math.sqrt(forward * forward + side * side);
		if (dist > 0.0) {
			forward /= dist;
			side /= dist;
		}
		forward *= accel;
		side *= accel;

		velX += forward * Math.cos(yawRad) + side * Math.cos(yawRad + Math.PI / 2.0);
		velZ += forward * Math.sin(yawRad) + side * Math.sin(yawRad + Math.PI / 2.0);
		velX *= friction;
		velZ *= friction;

		if (onGround && controller.doJump()) {
			velY = jumpHeight;
		}
	}
}
