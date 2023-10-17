package io.bluestaggo.voxelthing.gui.screen;

import io.bluestaggo.voxelthing.Game;
import io.bluestaggo.voxelthing.assets.Texture;
import io.bluestaggo.voxelthing.gui.control.*;
import io.bluestaggo.voxelthing.renderer.GLState;
import io.bluestaggo.voxelthing.renderer.MainRenderer;
import io.bluestaggo.voxelthing.renderer.draw.Quad;
import io.bluestaggo.voxelthing.settings.Setting;
import org.lwjgl.opengl.GL33C;

import java.util.HashMap;
import java.util.Map;

public class SettingsMenu extends GuiScreen {
	private final Control backButton;
	private final ScrollContainer settingList;
	private final Map<Control, Setting<?>> settingButtons = new HashMap<>();

	public SettingsMenu(Game game) {
		super(game);

		backButton = addControl(new LabeledButton(this)
				.withText("< Back")
				.at(5, 5)
				.size(100, 20)
		);
		settingList = (ScrollContainer) addControl(new ScrollContainer(this)
				.at(0, 30)
				.size(0, -30)
				.alignedSize(1.0f, 1.0f)
		);

		for (var category : game.settings.byCategory.entrySet()) {
			settingList.addControl(new Label(this)
					.withText(category.getKey())
					.withFont(game.renderer.fonts.outlined)
					.at(5, 0)
					.size(0, 20)
					.alignedSize(1.0f, 0.0f)
			);
			settingList.addPadding(5);

			for (Setting<?> setting : category.getValue()) {
				Container settingPanel = (Container) new Container(this)
						.size(0, 20)
						.alignedAt(0.1f, 0.0f)
						.alignedSize(0.8f, 0.0f);

				settingPanel.addControl(new Label(this)
						.withText(setting.name)
						.textAlignedAt(1.0f, 0.5f)
						.at(-5, 0)
						.alignedAt(0.5f, 0.0f)
						.alignedSize(0.0f, 1.0f)
				);

				Control settingButton = setting.getControl(this);
				settingButtons.put(settingButton, setting);
				settingPanel.addControl(settingButton);
				settingList.addControl(settingPanel);
				settingList.addPadding(5);
			}
		}
	}

	@Override
	public void draw() {
		MainRenderer r = game.renderer;

		Texture bgTex = r.textures.getTexture("/assets/gui/background.png");
		r.draw2D.drawQuad(Quad.shared()
				.size(r.screen.getWidth(), 30.0f)
				.withUV(0.0f, 0.0f, r.screen.getWidth() / (float)bgTex.width, 30.0f / bgTex.height)
				.withTexture(bgTex)
		);

		try (var state = new GLState()) {
			state.enable(GL33C.GL_BLEND);
			r.draw2D.drawQuad(Quad.shared()
					.at(0.0f, 30.0f)
					.size(r.screen.getWidth(), r.screen.getHeight() - 30.0f)
					.withColor(0.0f, 0.0f, 0.0f, 0.5f)
			);

			r.fonts.outlined.printCentered("SETTINGS", r.screen.getWidth() / 2.0f, 10.0f);
		}

		super.draw();
	}

	@Override
	public void onControlClicked(Control control, int button) {
		if (control == backButton) {
			game.closeGui();
		} else if (settingButtons.containsKey(control)) {
			settingButtons.get(control).handleControl(control);
		}
	}

	@Override
	protected void onMouseDragged(int button, int mx, int my) {
		super.onMouseDragged(button, mx, my);

		FocusableControl focusable = getFocusedControl();
		if (settingButtons.containsKey(focusable)) {
			Setting<?> setting = settingButtons.get(focusable);
			if (setting.isModifiableOnDrag()) {
				setting.handleControl(focusable);
			}
		}
	}

	@Override
	protected void onControlUnfocused(FocusableControl focusable) {
		if (settingButtons.containsKey(focusable)) {
			settingButtons.get(focusable).handleControl(focusable);
		}
	}

	@Override
	protected void onMouseScrolled(double scroll) {
		settingList.scroll(scroll * -10.0);
	}

	@Override
	public boolean pauseGame() {
		return true;
	}

	@Override
	public void onClosed() {
		game.settings.save();
	}
}
