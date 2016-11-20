package de.xima.fc.form.expression.enums;

public enum EScopeSource {
	EXTERNAL_CONTEXT(-1),
	LIBRARY(-2),
	UNRESOLVED(-3);
	
	private final int sourceId;
	private EScopeSource(final int sourceId) {
		this.sourceId = sourceId;
	}
	public int getSourceId() {
		return sourceId;
	}
	public final static int ID_UNRESOLVED = -3;
	public final static int ID_LIBRARY = -2;
	public final static int ID_EXTERNAL_CONTEXT = -1;
}