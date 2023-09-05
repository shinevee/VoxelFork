package io.bluestaggo.voxelthing.world.storage;

import io.bluestaggo.pds.CompoundItem;
import io.bluestaggo.pds.StructureItem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FolderSaveHandler implements ISaveHandler {
	private final Path worldFile;
	private final Path playerFile;
	private final Path chunkFolder;

	public FolderSaveHandler(Path root) throws IOException {
		worldFile = root.resolve("world.dat");
		playerFile = root.resolve("player.dat");
		chunkFolder = root.resolve("chunks");

		Files.createDirectories(root);
		Files.createDirectories(chunkFolder);
	}

	private Path getChunkPath(int x, int y, int z) {
		return chunkFolder.resolve(x + "_" + y + "_" + z + ".dat");
	}

	@Override
	public CompoundItem loadWorldData() {
		try {
			var item = StructureItem.readItemFromPath(worldFile);
			if (item instanceof CompoundItem compoundItem) {
				return compoundItem;
			}
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public void saveWorldData(CompoundItem data) {
		try {
			data.writeItemToPath(worldFile);
		} catch (IOException e) {
			System.out.println("Failed to save world data to \"" + worldFile + "\"");
			e.printStackTrace();
		}
	}

	@Override
	public CompoundItem loadPlayerData() {
		try {
			var item = StructureItem.readItemFromPath(playerFile);
			if (item instanceof CompoundItem compoundItem) {
				return compoundItem;
			}
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public void savePlayerData(CompoundItem data) {
		try {
			data.writeItemToPath(playerFile);
		} catch (IOException e) {
			System.out.println("Failed to save player data to \"" + playerFile + "\"");
			e.printStackTrace();
		}
	}

	@Override
	public CompoundItem loadChunkData(int x, int y, int z) {
		try {
			var item = StructureItem.readItemFromPath(getChunkPath(x, y, z));
			if (item instanceof CompoundItem compoundItem) {
				return compoundItem;
			}
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public void saveChunkData(int x, int y, int z, CompoundItem data) {
		Path path = getChunkPath(x, y, z);
		try {
			data.writeItemToPath(path);
		} catch (IOException e) {
			System.out.println("Failed to save chunk to \"" + path + "\"");
			e.printStackTrace();
		}
	}
}
