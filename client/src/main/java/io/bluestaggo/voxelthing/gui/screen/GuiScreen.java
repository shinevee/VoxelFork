package io.bluestaggo.voxelthing.gui.screen;

import io.bluestaggo.voxelthing.Game;
import io.bluestaggo.voxelthing.gui.control.Control;
import io.bluestaggo.voxelthing.gui.control.FocusableControl;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public abstract class GuiScreen {
	public final Game game;
	public final GuiScreen parent;

	private final List<Control> controls = new ArrayList<>();
	private FocusableControl focusedControl;

	public GuiScreen(Game game) {
		this.game = game;
		this.parent = game.getCurrentGui();
	}

	public Control addControl(Control control) {
		controls.add(control);
		return control;
	}

	public void draw() {
		for (Control control : controls) {
			control.draw();
		}
	}

	public void tick() {
	}

	public void handleInput() {
		for (int key : game.window.getKeysJustPressed()) {
			onKeyPressed(key);
			if (focusedControl != null) {
				focusedControl.onKeyPressed(key);
			}
		}

		for (char c : game.window.getCharactersPressed()) {
			onCharacterTyped(c);
			if (focusedControl != null) {
				focusedControl.onCharacterTyped(c);
			}
		}

		for (int mouse : game.window.getMouseButtonsJustPressed()) {
			onMouseClicked(mouse, game.renderer.screen.getMouseX(), game.renderer.screen.getMouseY());
		}

		boolean mouseDown = false;
		for (int mouse : game.window.getMouseButtonsHeld()) {
			mouseDown = true;
			onMouseDragged(mouse, game.renderer.screen.getMouseX(), game.renderer.screen.getMouseY());
		}

		if (!mouseDown && focusedControl != null && focusedControl.dragFocusOnly()) {
			FocusableControl oldFocused = focusedControl;
			focusedControl = null;
			onControlUnfocused(oldFocused);
		}

		if (game.window.getScrollY() != 0.0) {
			onMouseScrolled(game.window.getScrollY());
		}
	}

	protected void onKeyPressed(int key) {
		if (key == GLFW.GLFW_KEY_ESCAPE) {
			game.closeGui();
		}
	}

	protected void onCharacterTyped(char c) {
	}

	protected void onMouseClicked(int button, int mx, int my) {
		FocusableControl oldFocused = focusedControl;
		focusedControl = null;

		for (Control control : controls) {
			control.checkMouseClicked(button, mx, my);
			if (control instanceof FocusableControl focusable && control.intersects(mx, my)) {
				focusedControl = focusable;
			}
		}

		if (focusedControl != oldFocused && oldFocused != null) {
			onControlUnfocused(oldFocused);
		}
	}

	protected void onMouseDragged(int button, int mx, int my) {
		if (focusedControl != null) {
			focusedControl.onMouseDragged(button, mx, my);
		}
	}

	protected void onMouseScrolled(double scroll) {
	}

	public void onControlClicked(Control control, int button) {
	}

	public void focusControl(FocusableControl focusable) {
		FocusableControl oldFocused = focusedControl;
		focusedControl = focusable;
		if (oldFocused != focusable) {
			onControlUnfocused(focusable);
		}
	}

	protected void onControlUnfocused(FocusableControl focusable) {
	}

	public boolean isFocused(FocusableControl focusable) {
		return focusable == focusedControl;
	}

	public FocusableControl getFocusedControl() {
		return focusedControl;
	}

	public boolean pauseGame() {
		return false;
	}

	public void onClosed() {
	}
}
