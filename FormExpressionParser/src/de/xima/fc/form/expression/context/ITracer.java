package de.xima.fc.form.expression.context;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ITracer<T extends ITraceElement> {
	public void setCurrentlyProcessed(@Nullable T position);
	@Nullable
	public T getCurrentlyProcessed();	

	/**
	 * Descend one stack trace level, ie. when calling a function.
	 * @param object Current position.
	 */
	public void descend(@NotNull T position);
	/**
	 * Ascend from a stack trace level, ie. when returning form a function.
	 */
	public void ascend();	

	/**
	 * This is called when exceptions occur etc., ie. it does not need to be fast.
	 * @return The stack trace. May be empty, but not <code>null</code>.
	 */
	@NotNull
	public T[] getStackTrace();	
}
