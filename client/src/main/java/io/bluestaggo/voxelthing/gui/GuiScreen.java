package io.bluestaggo.voxelthing.gui;

import io.bluestaggo.voxelthing.Game;
import io.bluestaggo.voxelthing.gui.control.GuiControl;

import java.util.ArrayList;
import java.util.List;

public abstract class GuiScreen {
	public final Game game;
	private final List<GuiControl> controls = new ArrayList<>();

	public GuiScreen(Game game) {
		this.game = game;
	}

	public GuiControl addControl(GuiControl control) {
		controls.add(control);
		return control;
	}

	public void draw() {
		for (GuiControl control : controls) {
			control.draw();
		}
	}

	public void tick() {
	}

	public void handleInput() {
		for (int key : game.window.getKeysJustPressed()) {
			onKeyPressed(key);
		}

		for (int mouse : game.window.getMouseButtonsJustPressed()) {
			onMouseClicked(mouse, game.renderer.screen.getMouseX(), game.renderer.screen.getMouseY());
		}
	}

	protected void onKeyPressed(int key) {
	}

	protected void onMouseClicked(int button, int mx, int my) {
		for (GuiControl control : controls) {
			if (control.intersects(mx, my)) {
				control.onClick(button);
			}
		}
	}

	public void onControlClicked(GuiControl control, int button) {
	}
}
