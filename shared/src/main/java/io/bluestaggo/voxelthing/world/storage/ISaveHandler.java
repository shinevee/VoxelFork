package io.bluestaggo.voxelthing.world.storage;

import io.bluestaggo.pds.CompoundItem;

public interface ISaveHandler {
	CompoundItem loadWorldData();
	void saveWorldData(CompoundItem data);
	CompoundItem loadPlayerData();
	void savePlayerData(CompoundItem data);
	CompoundItem loadChunkData(int x, int y, int z);
	void saveChunkData(int x, int y, int z, CompoundItem data);
}
