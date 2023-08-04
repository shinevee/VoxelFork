package io.bluestaggo.voxelthing.window;

public class KeyState {
	private boolean pressed;
	private boolean wasPressed;

	void update() {
		this.wasPressed = pressed;
	}

	void setPressed(boolean pressed) {
		this.pressed = pressed;
	}

	public boolean isPressed() {
		return pressed;
	}

	public boolean wasPressed() {
		return wasPressed;
	}

	public boolean justPressed() {
		return pressed && !wasPressed;
	}

	@Override
	public String toString() {
		return "KeyState {"
				+ "\tisPressed: " + isPressed()
				+ "\twasPressed: " + wasPressed()
				+ "\tjustPressed: " + justPressed()
				+ "}";
	}
}
