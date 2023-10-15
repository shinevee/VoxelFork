package io.bluestaggo.voxelthing.gui.screen;

import io.bluestaggo.voxelthing.Game;
import io.bluestaggo.voxelthing.gui.control.Control;
import io.bluestaggo.voxelthing.gui.control.LabeledButton;
import io.bluestaggo.voxelthing.renderer.GLState;
import io.bluestaggo.voxelthing.renderer.MainRenderer;
import io.bluestaggo.voxelthing.renderer.draw.Quad;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL33C;

public class PauseMenu extends GuiScreen {
	private final Control backButton;
	private final Control settingsButton;
	private final Control exitButton;

	public PauseMenu(Game game) {
		super(game);

		backButton = addControl(new LabeledButton(this)
				.withText("Back to Game")
				.at(-50.0f, -20.0f)
				.size(100.0f, 20.0f)
				.alignedAt(0.5f, 0.5f)
		);
		settingsButton = addControl(new LabeledButton(this)
				.withText("Settings")
				.at(-50.0f, 10.0f)
				.size(100.0f, 20.0f)
				.alignedAt(0.5f, 0.5f)
		);
		exitButton = addControl(new LabeledButton(this)
				.withText("Exit World")
				.at(-50.0f, 35.0f)
				.size(100.0f, 20.0f)
				.alignedAt(0.5f, 0.5f)
		);
	}

	@Override
	public void draw() {
		MainRenderer r = game.renderer;
		try (var state = new GLState()) {
			state.enable(GL33C.GL_BLEND);
			r.draw2D.drawQuad(Quad.shared()
					.size(r.screen.getWidth(), r.screen.getHeight())
					.withColor(0.0f, 0.0f, 0.0f, 0.5f)
			);
		}

		r.fonts.outlined.printCentered("PAUSE", r.screen.getWidth() / 2.0f, r.screen.getHeight() / 4.0f - 16.0f, 1.0f, 1.0f, 1.0f, 2.0f);

		super.draw();
	}

	@Override
	public void onControlClicked(Control control, int button) {
		if (control == backButton) {
			game.closeGui();
		} else if (control == settingsButton) {
			game.openGui(new SettingsMenu(this.game));
		} else if (control == exitButton) {
			game.closeGui();
			game.exitWorld();
		}
	}

	@Override
	protected void onKeyPressed(int key) {
		if (key == GLFW.GLFW_KEY_ESCAPE) {
			game.closeGui();
		}
	}

	@Override
	public boolean pauseGame() {
		return true;
	}
}
