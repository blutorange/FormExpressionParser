package de.xima.fc.form.expression.iface.evaluate;

import javax.annotation.Nullable;

public interface ILogger {
	public void error(@Nullable String message);
	public void warn(@Nullable String message);
	public void info(@Nullable String message);
	public void debug(@Nullable String message);
}
