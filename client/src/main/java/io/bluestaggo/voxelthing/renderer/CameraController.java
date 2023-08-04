package io.bluestaggo.voxelthing.renderer;

public interface CameraController {
	boolean moveForward();
	boolean moveBackward();
	boolean moveLeft();
	boolean moveRight();
	float moveYaw();
	float movePitch();

	default float getSpeed() {
		return 10.0f;
	}

	default float getSensitivity() {
		return 0.1f;
	}
}
