package io.bluestaggo.voxelthing;

import io.bluestaggo.pds.CompoundItem;
import io.bluestaggo.pds.StringItem;
import io.bluestaggo.pds.StructureItem;

public class Identifier {
	public final String namespace;
	public final String name;
	public final String fullName;

	public Identifier(String name) {
		this("vt", name);
	}

	public Identifier(String namespace, String name) {
		this.namespace = namespace;
		this.name = name;
		this.fullName = namespace + ":" + name;
	}

	@Override
	public String toString() {
		return fullName;
	}

	public StructureItem serialize() {
		var item = new CompoundItem();
		item.map.put("namespace", new StringItem(namespace));
		item.map.put("name", new StringItem(name));
		return item;
	}
}
