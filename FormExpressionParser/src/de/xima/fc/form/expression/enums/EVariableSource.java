package de.xima.fc.form.expression.enums;

public enum EVariableSource {
	SYMBOL_TABLE(0),
	EXTERNAL_CONTEXT(-1),
	LIBRARY(-2),
	UNRESOLVED(-3);	
	public final int sourceId;
	private EVariableSource(final int sourceId) {
		this.sourceId = sourceId;
	}
	public final static int ID_UNRESOLVED = -3;
	public final static int ID_LIBRARY = -2;
	public final static int ID_EXTERNAL_CONTEXT = -1;
}