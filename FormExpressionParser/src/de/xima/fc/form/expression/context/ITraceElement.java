package de.xima.fc.form.expression.context;

public interface ITraceElement {
	public int getStartLine();
	public int getStartColumn();
	public int getEndLine();
	public int getEndColumn();
	public String getPositionName();
}
