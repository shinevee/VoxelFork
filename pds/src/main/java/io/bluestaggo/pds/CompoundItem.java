package io.bluestaggo.pds;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CompoundItem extends StructureItem {
	public final Map<String, StructureItem> map;

	public CompoundItem() {
		this(new HashMap<>());
	}

	public CompoundItem(Map<String, StructureItem> list) {
		this.map = list;
	}

	@Override
	public Map<String, StructureItem> getMap() {
		return map;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(map.size() + " items:");
		for (var item : map.entrySet()) {
			builder.append("\n").append(item.getKey()).append(": ").append(item.getValue());
		}
		builder.append("\nend");
		return builder.toString();
	}

	@Override
	protected void read(DataInputStream stream) throws IOException {
		int length = stream.readInt();
		for (int i = 0; i < length; i++) {
			map.put(readString(stream), readItem(stream));
		}
	}

	@Override
	protected void write(DataOutputStream stream) throws IOException {
		stream.writeInt(map.size());
		for (var item : map.entrySet()) {
			writeString(item.getKey(), stream);
			writeItem(item.getValue(), stream);
		}
	}
}
