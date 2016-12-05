package de.xima.fc.form.expression.iface.evaluate;

import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.iface.IReset;

public interface ITracer<T extends ITraceElement> extends IReset {
	public void setCurrentlyProcessed(@Nullable T position);
	@Nullable
	public T getCurrentlyProcessed();
	public void appendWarning(@Nonnull IEvaluationWarning warning);
	@Nonnull
	public Collection<IEvaluationWarning> getWarnings();
	public void enableWarnings();
	public void disableWarnings();
	public boolean isWarningsEnabled();
	
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
