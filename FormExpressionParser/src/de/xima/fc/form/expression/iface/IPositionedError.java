package de.xima.fc.form.expression.iface;

public interface IPositionedError {
	public boolean isPositionInformationAvailable();
	public int getBeginLine();
	public int getEndLine();
	public int getBeginColumn();
	public int getEndColumn();
	public String getMessage();
}