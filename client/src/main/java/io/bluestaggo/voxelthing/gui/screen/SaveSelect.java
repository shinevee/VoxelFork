package io.bluestaggo.voxelthing.gui.screen;

import io.bluestaggo.voxelthing.Game;
import io.bluestaggo.voxelthing.gui.control.Control;
import io.bluestaggo.voxelthing.gui.control.LabeledButton;
import io.bluestaggo.voxelthing.gui.control.ScrollContainer;
import io.bluestaggo.voxelthing.gui.control.WorldPanel;
import io.bluestaggo.voxelthing.renderer.GLState;
import io.bluestaggo.voxelthing.renderer.MainRenderer;
import io.bluestaggo.voxelthing.renderer.draw.Quad;
import io.bluestaggo.voxelthing.world.storage.FolderSaveHandler;
import org.lwjgl.opengl.GL33C;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Stream;

public class SaveSelect extends GuiScreen {
	private final Control backButton;
	private final Control newWorldButton;
	private final ScrollContainer worldContainer;

	public SaveSelect(Game game) {
		super(game);

		backButton = addControl(new LabeledButton(this)
				.withText("Back")
				.at(-105, -25)
				.size(100, 20)
				.alignedAt(0.5f, 1.0f)
		);
		newWorldButton = addControl(new LabeledButton(this)
				.withText("New World")
				.at(5, -25)
				.size(100, 20)
				.alignedAt(0.5f, 1.0f)
		);
		worldContainer = (ScrollContainer) addControl(new ScrollContainer(this)
				.at(0, 30)
				.size(0, -60)
				.alignedSize(1.0f, 1.0f)
		);

		refresh();
	}

	public void refresh() {
		worldContainer.clearControls();
		try (Stream<Path> worlds = Files.list(game.worldDir)) {
			worlds.filter(Files::isDirectory)
					.map(p -> {
						try {
							return new FolderSaveHandler(p);
						} catch (IOException e) {
							return null;
						}
					})
					.filter(Objects::nonNull)
					.map(s -> new WorldPanel(this, s)
							.at(-75, 0)
							.size(150, 20)
							.alignedAt(0.5f, 0.0f)
					)
					.forEach(worldContainer::addControl);
		} catch (IOException e) {
		}
	}

	@Override
	public void draw() {
		MainRenderer r = game.renderer;

		try (var state = new GLState()) {
			state.enable(GL33C.GL_BLEND);
			r.draw2D.drawQuad(Quad.shared()
					.at(0, 30)
					.size(r.screen.getWidth(), r.screen.getHeight() - 60)
					.withColor(0.0f, 0.0f, 0.0f, 0.5f));

			r.fonts.outlined.printCentered("SELECT WORLD", r.screen.getWidth() / 2.0f, 10.0f);
		}

		super.draw();
	}

	@Override
	public void onControlClicked(Control control, int button) {
		if (control instanceof WorldPanel world) {
			switch (button) {
				case 0:
					game.startWorld(world.saveHandler);
					break;

				case 1:
					game.openGui(new DeleteWorld(game, world.saveHandler, world.worldName));
					break;
			}
		} else if (control == backButton) {
			game.closeGui();
		} else if (control == newWorldButton) {
			game.openGui(new CreateWorld(game));
		}
	}

	@Override
	protected void onMouseScrolled(double scroll) {
		worldContainer.scroll(scroll * -10.0);
	}
}
