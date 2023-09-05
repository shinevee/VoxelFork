package io.bluestaggo.voxelthing.world;

import io.bluestaggo.pds.CompoundItem;
import io.bluestaggo.pds.LongItem;

public class WorldInfo {
	public long seed;

	public void deserialize(CompoundItem data) {
		seed = data.map.get("seed").getLong();
	}

	public CompoundItem serialize() {
		var data = new CompoundItem();
		data.map.put("seed", new LongItem(seed));
		return data;
	}
}
