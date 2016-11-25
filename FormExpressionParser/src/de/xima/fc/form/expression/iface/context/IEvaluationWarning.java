package de.xima.fc.form.expression.iface.context;

import javax.annotation.Nonnull;

public interface IEvaluationWarning {
	@Nonnull
	public String getMessage();
	public int getLine();
	public int getColumn();
}