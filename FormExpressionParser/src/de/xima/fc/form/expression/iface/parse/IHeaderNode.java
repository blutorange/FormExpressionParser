package de.xima.fc.form.expression.iface.parse;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.node.ASTVariableDeclarationClauseNode;

public interface IHeaderNode extends ISourceResolvable, Serializable {
	/**
	 * @return The node with the value. Must not be an
	 *         {@link ASTVariableDeclarationClauseNode}
	 */
	@Nonnull
	public Node getNode();
	
	public boolean hasNode();

	/** @return Whether it is a function declaration or variable declaration. */
	public boolean isFunction();

	@Nullable
	public Node getType();
}