package io.bluestaggo.voxelthing.world.storage;

import io.bluestaggo.pds.CompoundItem;
import io.bluestaggo.pds.StructureItem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

public class FolderSaveHandler implements ISaveHandler {
	private final Path root;
	private final Path chunkFolder;

	public FolderSaveHandler(Path root) throws IOException {
		this.root = root;
		chunkFolder = root.resolve("chunks");

		Files.createDirectories(root);
		Files.createDirectories(chunkFolder);
	}

	private Path getChunkPath(int x, int y, int z) {
		return chunkFolder.resolve(x + "_" + y + "_" + z + ".dat");
	}

	@Override
	public CompoundItem loadData(String type) {
		Path file = root.resolve(type + ".dat");
		try {
			var item = StructureItem.readItemFromPath(file, false);
			if (item instanceof CompoundItem compoundItem) {
				return compoundItem;
			}
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public void saveData(String type, CompoundItem data) {
		Path file = root.resolve(type + ".dat");
		try {
			data.writeItemToPath(file, false);
		} catch (IOException e) {
			System.out.println("Failed to save world data to \"" + file + "\"");
			e.printStackTrace();
		}
	}

	@Override
	public CompoundItem loadChunkData(int x, int y, int z) {
		try {
			var item = StructureItem.readItemFromPath(getChunkPath(x, y, z), true);
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
			data.writeItemToPath(path, true);
		} catch (IOException e) {
			System.out.println("Failed to save chunk to \"" + path + "\"");
			e.printStackTrace();
		}
	}

	@Override
	public void delete() throws IOException {
		try (Stream<Path> files = Files.walk(root)) {
			files.sorted(Comparator.reverseOrder())
					.forEach(p -> {
						try {
							Files.delete(p);
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					});
		} catch (RuntimeException e) {
			if (e.getCause() instanceof IOException ioException) {
				throw ioException;
			}
			throw e;
		}
	}
}
