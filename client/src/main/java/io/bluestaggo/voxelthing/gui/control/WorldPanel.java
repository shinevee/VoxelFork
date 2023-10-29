package io.bluestaggo.voxelthing.gui.control;

import io.bluestaggo.pds.CompoundItem;
import io.bluestaggo.pds.StringItem;
import io.bluestaggo.voxelthing.gui.Icon;
import io.bluestaggo.voxelthing.gui.screen.GuiScreen;
import io.bluestaggo.voxelthing.world.storage.ISaveHandler;

public class WorldPanel extends Container {
	public final ISaveHandler saveHandler;
	public final String worldName;
	private final Control label;
	private final Control deleteButton;

	public WorldPanel(GuiScreen screen, ISaveHandler saveHandler) {
		super(screen);

		this.saveHandler = saveHandler;

		CompoundItem data = saveHandler.loadData("world");
		if (data == null) {
			data = new CompoundItem();
		}
		worldName = data.map.get("name") instanceof StringItem nameItem ? nameItem.value : "???";

		label = this.addControl(new LabeledButton(screen)
				.withText(worldName)
				.size(-20, 0)
				.alignedSize(1, 1)
		);
		deleteButton = this.addControl(new IconButton(screen)
				.withIcon(Icon.DELETE)
				.at(-20, 0)
				.size(20, 0)
				.alignedSize(0, 1)
				.alignedAt(1, 0)
		);
	}

	@Override
	public void onControlClicked(Control control, int button) {
		super.onControlClicked(this, control == label ? 0 : control == deleteButton ? 1 : -1);
	}
}
