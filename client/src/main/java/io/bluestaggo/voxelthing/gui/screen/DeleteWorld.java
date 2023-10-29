package io.bluestaggo.voxelthing.gui.screen;

import io.bluestaggo.voxelthing.Game;
import io.bluestaggo.voxelthing.gui.control.Control;
import io.bluestaggo.voxelthing.gui.control.Label;
import io.bluestaggo.voxelthing.gui.control.LabeledButton;
import io.bluestaggo.voxelthing.world.storage.ISaveHandler;

import java.io.IOException;

public class DeleteWorld extends GuiScreen {
	private final ISaveHandler saveHandler;
	private final Control yesButton;

	public DeleteWorld(Game game, ISaveHandler saveHandler, String worldName) {
		super(game);
		this.saveHandler = saveHandler;

		addControl(new Label(this)
				.withText("Are you sure you want to\ndelete \"" + worldName + "\"?")
				.at(0.0f, -10.0f)
				.alignedAt(0.5f, 0.5f)
		);

		addControl(new LabeledButton(this)
				.withText("Cancel")
				.at(-60.0f, 10.0f)
				.size(50.0f, 20.0f)
				.alignedAt(0.5f, 0.5f)
		);
		yesButton = addControl(new LabeledButton(this)
				.withText("Delete")
				.at(10.0f, 10.0f)
				.size(50.0f, 20.0f)
				.alignedAt(0.5f, 0.5f)
		);
	}

	public void onControlClicked(Control control, int button) {
		if (control == yesButton) {
			try {
				saveHandler.delete();
			} catch (IOException e) {
				System.err.println("Failed to delete world!");
				e.printStackTrace();
			}

			if (parent instanceof SaveSelect saveSelect) {
				saveSelect.refresh();
			}
		}
		game.closeGui();
	}
}
