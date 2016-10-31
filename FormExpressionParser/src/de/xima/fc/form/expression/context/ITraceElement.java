package de.xima.fc.form.expression.context;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.grammar.Node;

/**
 * A stack trace element with information about the called method and its source code location.
 * This interface is implemented by {@link Node}. Effectively this means we keep a history
 * of the visited nodes.
 * @see ITracer
 * @author madgaksha
 */
public interface ITraceElement {
	public int getStartLine();
	public int getStartColumn();
	public int getEndLine();
	public int getEndColumn();
	@Nonnull public String getMethodName();
}
