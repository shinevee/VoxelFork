package io.bluestaggo.voxelthing.window;

import io.bluestaggo.voxelthing.Game;
import io.bluestaggo.voxelthing.world.entity.IPlayerController;

import static org.lwjgl.glfw.GLFW.*;

public class ClientPlayerController implements IPlayerController {
	private final Game game;

	public ClientPlayerController(Game game) {
		this.game = game;
	}

	@Override
	public double moveForward() {
		return game.isGuiOpen() ? 0.0
				: game.window.isKeyDown(GLFW_KEY_W) ? 1.0
				: game.window.isKeyDown(GLFW_KEY_S) ? -1.0
				: 0.0;
	}

	@Override
	public double moveSide() {
		return game.isGuiOpen() ? 0.0
				: game.window.isKeyDown(GLFW_KEY_D) ? 1.0
				: game.window.isKeyDown(GLFW_KEY_A) ? -1.0
				: 0.0;
	}

	@Override
	public double moveYaw() {
		return game.window.isCursorGrabbed() && !game.isGuiOpen() ? game.window.getMouseDeltaX() * game.mouseSensitivity : 0.0f;
	}

	@Override
	public double movePitch() {
		return game.window.isCursorGrabbed() && !game.isGuiOpen() ? game.window.getMouseDeltaY() * game.mouseSensitivity : 0.0f;
	}

	@Override
	public boolean doJump() {
		return game.window.isKeyDown(GLFW_KEY_SPACE);
	}

	@Override
	public boolean doCrouch() {
		return game.window.isKeyDown(GLFW_KEY_LEFT_SHIFT);
	}
}
