package io.bluestaggo.voxelthing.gui;

import io.bluestaggo.voxelthing.Game;
import io.bluestaggo.voxelthing.gui.control.GuiControl;
import io.bluestaggo.voxelthing.gui.control.LabeledButton;
import io.bluestaggo.voxelthing.renderer.GLState;
import io.bluestaggo.voxelthing.renderer.MainRenderer;
import io.bluestaggo.voxelthing.renderer.draw.Quad;

import static org.lwjgl.opengl.GL11.GL_BLEND;

public class SaveSelect extends GuiScreen {
	private final GuiControl newWorldButton;
	private final GuiControl playWorldButton;

	public SaveSelect(Game game) {
		super(game);

		newWorldButton = addControl(new LabeledButton(this)
				.withText("New World")
				.at(-50.0f, -25.0f)
				.size(100.0f, 20.0f)
				.alignedAt(0.5f, 1.0f)
		);
		playWorldButton = addControl(new LabeledButton(this)
				.withText("Play Saved World")
				.at(-50.0f, -10.0f)
				.size(100.0f, 20.0f)
				.alignedAt(0.5f, 0.5f)
		);
	}

	@Override
	public void draw() {
		MainRenderer r = game.renderer;

		try (var state = new GLState()) {
			state.enable(GL_BLEND);
			r.draw2D.drawQuad(Quad.shared()
					.at(0.0f, 30.0f)
					.size(r.screen.getWidth(), r.screen.getHeight() - 60.0f)
					.withColor(0.0f, 0.0f, 0.0f, 0.5f));

			r.fonts.outlined.printCentered("SELECT WORLD", r.screen.getWidth() / 2.0f, 10.0f);
		}

		super.draw();
	}

	@Override
	public void onControlClicked(GuiControl control, int button) {
		if (control == newWorldButton) {
			game.startWorld();
			game.openGui(null);
		}

		if (control == playWorldButton) {
			game.startWorld("world");
			game.openGui(null);
		}
	}
}
