package io.bluestaggo.voxelthing.world.entity;

public interface IPlayerController {
	double moveForward();
	double moveSide();
	double moveYaw();
	double movePitch();
	boolean doJump();
}
