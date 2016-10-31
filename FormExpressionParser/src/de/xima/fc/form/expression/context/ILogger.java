package de.xima.fc.form.expression.context;

import javax.annotation.Nullable;

public interface ILogger {
	public void error(@Nullable String message);
	public void warn(@Nullable String message);
	public void info(@Nullable String message);
	public void debug(@Nullable String message);
}
