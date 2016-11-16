package de.xima.fc.form.expression.iface.context;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.util.IReset;

public interface ITracer<T extends ITraceElement> extends IReset {
	public void setCurrentlyProcessed(@Nullable T position);
	@javax.annotation.Nullable
	public T getCurrentlyProcessed();

	/**
	 * Descend one stack trace level, ie. when calling a function.
	 * @param position Current position.
	 */
	public void descend(@Nonnull T position);

	/**
	 * Ascend from a stack trace level, ie. when returning form a function.
	 */
	public void ascend();

	/**
	 * This is called when exceptions occur etc., ie. it does not need to be fast.
	 * @return The stack trace. May be empty, but not <code>null</code>.
	 */
	@Nonnull
	public T[] getStackTrace();
}
