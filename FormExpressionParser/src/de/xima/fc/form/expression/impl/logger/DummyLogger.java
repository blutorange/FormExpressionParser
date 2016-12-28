package de.xima.fc.form.expression.impl.logger;

import javax.annotation.Nullable;

import de.xima.fc.form.expression.iface.evaluate.ILogger;

public enum DummyLogger implements ILogger {
	INSTANCE;
	@Override
	public void error(@Nullable final String message, @Nullable final Throwable cause) {
	}
	@Override
	public void warn(@Nullable final String message, @Nullable final Throwable cause) {
	}
	@Override
	public void info(@Nullable final String message, @Nullable final Throwable cause) {
	}
	@Override
	public void debug(@Nullable final String message, @Nullable final Throwable cause) {
	}
	@Override
	public void reset() {
	}
}