package de.xima.fc.form.expression.iface.parse;

import java.io.Serializable;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.node.ASTFunctionClauseNode;
import de.xima.fc.form.expression.node.ASTNullNode;
import de.xima.fc.form.expression.node.ASTVariableDeclarationClauseNode;

@NonNullByDefault
public interface IHeaderNode extends ISourceResolvable, Serializable, IVariableTyped {
	/**
	 * A variable declaration node return the
	 * {@link ASTVariableDeclarationClauseNode#getAssignmentNode()}, or an
	 * {@link ASTNullNode}. An {@link ASTFunctionClauseNode} returns itself,
	 * as the function is the value.
	 * A header node created on-the-fly returns an {@link ASTNullNode}.
	 *
	 * @return The node with the value. Must not be an
	 *         {@link ASTVariableDeclarationClauseNode}
	 */
	public Node getHeaderValueNode();

	/** @return Whether it is a function declaration or variable declaration. */
	public boolean isFunction();

	/** @return The node where this header was declared. */
	public Node getHeaderDeclarationNode();
}