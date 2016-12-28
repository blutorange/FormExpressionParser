package de.xima.fc.form.expression.iface.parse;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.grammar.Node;

@NonNullByDefault
public interface IArgumentResolvable {
	public int getArgumentCount();
	public ISourceResolvable getArgResolvable(int i);
	public Node getBodyNode();
	/** @return Whether the last argument is a var args arguments. */
	public boolean hasVarArgs();
}