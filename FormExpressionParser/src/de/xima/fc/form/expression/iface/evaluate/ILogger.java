package de.xima.fc.form.expression.iface.evaluate;

import javax.annotation.Nullable;
import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.iface.IReset;

@NonNullByDefault
public interface ILogger extends IReset {
	public void error(@Nullable String message, @Nullable Throwable cause);
	public void warn(@Nullable String message, @Nullable Throwable cause);
	public void info(@Nullable String message, @Nullable Throwable cause);
	public void debug(@Nullable String message, @Nullable Throwable cause);
}