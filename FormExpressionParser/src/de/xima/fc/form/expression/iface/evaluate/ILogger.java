package de.xima.fc.form.expression.iface.evaluate;

import javax.annotation.Nullable;

import de.xima.fc.form.expression.iface.IReset;

public interface ILogger extends IReset {
	public void error(@Nullable String message);
	public void warn(@Nullable String message);
	public void info(@Nullable String message);
	public void debug(@Nullable String message);
}