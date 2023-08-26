package io.bluestaggo.voxelthing.gui;

import io.bluestaggo.voxelthing.Game;

public abstract class GuiScreen {
	protected final Game game;

	public GuiScreen(Game game) {
		this.game = game;
	}

	public abstract void draw();

	public void tick() {
	}

	public void handleInput() {
		for (int key : game.window.getKeysJustPressed()) {
			onKeyPressed(key);
		}

		for (int mouse : game.window.getMouseButtonsJustPressed()) {
			onMouseClicked(mouse);
		}
	}

	protected void onKeyPressed(int key) {
	}

	protected void onMouseClicked(int key) {
	}
}
