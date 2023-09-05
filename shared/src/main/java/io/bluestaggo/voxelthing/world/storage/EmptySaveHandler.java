package io.bluestaggo.voxelthing.world.storage;

import io.bluestaggo.pds.CompoundItem;

public class EmptySaveHandler implements ISaveHandler {
	@Override
	public CompoundItem loadWorldData() {
		return null;
	}

	@Override
	public void saveWorldData(CompoundItem data) {
	}

	@Override
	public CompoundItem loadPlayerData() {
		return null;
	}

	@Override
	public void savePlayerData(CompoundItem data) {
	}

	@Override
	public CompoundItem loadChunkData(int x, int y, int z) {
		return null;
	}

	@Override
	public void saveChunkData(int x, int y, int z, CompoundItem data) {
	}
}
