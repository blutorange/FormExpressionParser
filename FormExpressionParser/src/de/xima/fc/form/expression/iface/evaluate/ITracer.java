package de.xima.fc.form.expression.iface.evaluate;

import javax.annotation.Nullable;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.common.collect.ImmutableList;

import de.xima.fc.form.expression.iface.IReset;

@NonNullByDefault
public interface ITracer<T extends ITraceElement> extends IReset {

	public void appendWarning(IEvaluationWarning warning);
	/** @return A (copied) list of warnings. This list may be modified safely. */

	public ImmutableList<IEvaluationWarning> buildWarnings();

	public void enableWarnings();
	public void disableWarnings();
	public boolean isWarningsEnabled();

	/** Descend one stack trace level, ie. when calling a function. */
	public void descend();

	/** Ascend from a stack trace level, ie. when returning form a function. */
	public void ascend();

	/**
	 * This is called when exceptions occur etc., ie. it does not need to be fast.
	 * @return The stack trace. May be empty, but not <code>null</code>.
	 */
	public T[] getStackTrace();

	@Nullable
	public T getCurrentlyProcessed();
	public void setCurrentlyProcessed(@Nullable T position);
}