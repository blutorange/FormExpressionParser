package de.xima.fc.form.expression.iface.parse;

import java.io.Serializable;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.node.ASTVariableDeclarationClauseNode;

@ParametersAreNonnullByDefault
public interface IHeaderNode extends ISourceResolvable, Serializable, IVariableTyped {
	/**
	 * @return The node with the value. Must not be an
	 *         {@link ASTVariableDeclarationClauseNode}
	 */
	public Node getNode();

	/** @return Whether it is a function declaration or variable declaration. */
	public boolean isFunction();

	/** @return The node where this header was declared. */
	public Node getDeclarationNode();
}