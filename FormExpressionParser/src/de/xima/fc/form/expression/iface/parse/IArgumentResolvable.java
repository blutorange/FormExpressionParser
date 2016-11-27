package de.xima.fc.form.expression.iface.parse;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.grammar.Node;

public interface IArgumentResolvable {
	public Node getArgumentNode(int i);
	public int getArgumentCount();
	@Nonnull
	public ISourceResolvable getArgResolvable(int i);
	@Nonnull
	public Node getBodyNode();
	@Nonnull
	public ISourceResolvable getThisResolvable();
	@Nonnull
	public ISourceResolvable getArgumentsResolvable();
}