package io.bluestaggo.voxelthing.settings;

import io.bluestaggo.pds.CompoundItem;
import io.bluestaggo.pds.StructureItem;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class Settings {
	private static final String[] CATEGORIES = {
			"Graphics",
			"Controls",
			"Misc"
	};

	public final Map<String, Setting<?>> bySaveName = new HashMap<>();
	public final Map<String, List<Setting<?>>> byCategory = new LinkedHashMap<>();

	{
		for (String category : CATEGORIES) {
			byCategory.put(category, new ArrayList<>());
		}
	}

	public Setting<Boolean> viewBobbing = addSetting(new ToggleSetting("Graphics", "View Bobbing", true));
	public Setting<String> controllerName = addSetting(new TextBoxSetting("Controls", "Controller Name", "Controller"));
	public Setting<Boolean> invertLook = addSetting(new ToggleSetting("Controls", "Invert Look", false));
	public Setting<String> name = addSetting(new TextBoxSetting("Misc", "Name", "Staggo"));

	private <T extends Setting<?>> T addSetting(T setting) {
		List<Setting<?>> settingList = byCategory.computeIfAbsent(setting.category, k -> new ArrayList<>());
		settingList.add(setting);
		bySaveName.put(setting.saveName, setting);
		return setting;
	}

	public void readFrom(Path path) {
		StructureItem data;

		try {
			data = StructureItem.readItemFromPath(path);
		} catch (IOException e) {
			return;
		}

		if (!(data instanceof CompoundItem)) {
			return;
		}

		for (Map.Entry<String, StructureItem> item : data.getMap().entrySet()) {
			Setting<?> setting = bySaveName.get(item.getKey());
			if (setting != null) {
				setting.deserialize(item.getValue());
			}
		}
	}
}
