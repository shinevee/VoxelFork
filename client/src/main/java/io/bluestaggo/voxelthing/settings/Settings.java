package io.bluestaggo.voxelthing.settings;

import io.bluestaggo.pds.CompoundItem;
import io.bluestaggo.pds.StructureItem;
import io.bluestaggo.voxelthing.Game;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;

public class Settings {
	public static final Function<Float, String> PERCENTAGE_TEXT_TRANSFORMER = value -> (int)(value * 100.0f) + "%";

	private static final String[] CATEGORIES = {
			"Graphics",
	};

	private Path savePath;

	private final Map<String, Setting<?>> bySaveNameMut = new HashMap<>();
	private final Map<String, List<Setting<?>>> byCategoryMut = new LinkedHashMap<>();
	private final Set<Setting<?>> settingsSetMut = new HashSet<>();

	public final Map<String, Setting<?>> bySaveName = Collections.unmodifiableMap(bySaveNameMut);
	public final Map<String, List<Setting<?>>> byCategory = Collections.unmodifiableMap(byCategoryMut);
	public final Set<Setting<?>> settingsSet = Collections.unmodifiableSet(settingsSetMut);

	{
		for (String category : CATEGORIES) {
			byCategoryMut.put(category, new ArrayList<>());
		}
	}

	public final Setting<Integer> limitFps = addSetting(new ChoiceSetting("Graphics", "Limit FPS", 2, "OFF", "ON", "Menu Only"));
	public final Setting<Integer> renderDistanceHor = addSetting(new IntSliderSetting("Graphics", "Horizontal Render Distance", 16, 1, 16));
	public final Setting<Integer> renderDistanceVer = addSetting(new IntSliderSetting("Graphics", "Vertical Render Distance", 8, 1, 16));
	public final Setting<Boolean> viewBobbing = addSetting(new ToggleSetting("Graphics", "View Bobbing", true));
	public final Setting<Boolean> thirdPerson = addSetting(new ToggleSetting("Graphics", "Third Person", false));
	public final Setting<Boolean> hideGui = addSetting(new ToggleSetting("Graphics", "Hide GUI", false));
	public final Setting<Integer> skin = addSetting(new ChoiceSetting("Graphics", "Skin", 0, Arrays.stream(Game.SKINS)
			.map(s -> (Character.toUpperCase(s.charAt(0)) + s.substring(1)).replace('_', ' '))
			.toArray(String[]::new)));
	public final Setting<Integer> guiScale = addSetting(new IntSliderSetting("Graphics", "GUI Scale", 0, 0, 4).setModifiableOnDrag(false));

	private <T extends Setting<?>> T addSetting(T setting) {
		List<Setting<?>> settingList = byCategoryMut.computeIfAbsent(setting.category, k -> new ArrayList<>());
		settingsSetMut.add(setting);
		settingList.add(setting);
		bySaveNameMut.put(setting.saveName, setting);
		return setting;
	}

	public void readFrom(Path path) {
		savePath = path;
		StructureItem data;

		try {
			data = StructureItem.readItemFromPath(path, false);
		} catch (IOException e) {
			return;
		}

		if (!(data instanceof CompoundItem)) {
			return;
		}

		for (Map.Entry<String, StructureItem> item : data.getMap().entrySet()) {
			Setting<?> setting = bySaveNameMut.get(item.getKey());
			if (setting != null) {
				setting.deserialize(item.getValue());
			}
		}
	}

	public void saveTo(Path path) {
		savePath = path;
		save();
	}

	public void save() {
		if (savePath == null) {
			return;
		}

		var data = new CompoundItem();

		for (Setting<?> setting : settingsSetMut) {
			data.setItem(setting.saveName, setting.serialize());
		}

		try {
			data.writeItemToPath(savePath, false);
		} catch (IOException e) {
			System.err.println("Failed to save settings!");
			e.printStackTrace();
		}
	}
}
