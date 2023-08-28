package io.bluestaggo.voxelthing;

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
}
