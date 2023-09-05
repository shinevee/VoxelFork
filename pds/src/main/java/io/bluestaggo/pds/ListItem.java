package io.bluestaggo.pds;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListItem extends StructureItem {
	public final List<StructureItem> list;

	public ListItem() {
		this(new ArrayList<>());
	}

	public ListItem(StructureItem... items) {
		this(new ArrayList<>(List.of(items)));
	}

	public ListItem(List<StructureItem> list) {
		this.list = list;
	}

	@Override
	public List<StructureItem> getList() {
		return list;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(list.size() + " items:");
		for (StructureItem item : list) {
			builder.append("\n").append(item);
		}
		builder.append("\nend");
		return builder.toString();
	}

	@Override
	protected void read(DataInputStream stream) throws IOException {
		int length = stream.readInt();
		for (int i = 0; i < length; i++) {
			list.add(readItem(stream));
		}
	}

	@Override
	protected void write(DataOutputStream stream) throws IOException {
		stream.writeInt(list.size());
		for (StructureItem item : list) {
			item.writeItem(stream);
		}
	}
}
