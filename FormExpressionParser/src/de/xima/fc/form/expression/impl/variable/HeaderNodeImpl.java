package de.xima.fc.form.expression.impl.variable;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;

import de.xima.fc.form.expression.enums.EVariableSource;
import de.xima.fc.form.expression.exception.FormExpressionException;
import de.xima.fc.form.expression.exception.parse.IllegalVariableSourceResolutionException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IHeaderNode;
import de.xima.fc.form.expression.node.ASTFunctionClauseNode;
import de.xima.fc.form.expression.node.ASTNullNode;
import de.xima.fc.form.expression.node.ASTVariableDeclarationClauseNode;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

@Immutable
@ParametersAreNonnullByDefault
public class HeaderNodeImpl implements IHeaderNode {
	private static final long serialVersionUID = 1L;

	private final Node node;
	private final String variableName;
	@Nullable
	private final Node typedNode;
	private final boolean isFunction;
	private int source = EVariableSource.ID_UNRESOLVED;

	public HeaderNodeImpl(final ASTVariableDeclarationClauseNode node) {
		this.node = node.hasAssignment() ? node.getAssignmentNode() : new ASTNullNode(node);
		this.typedNode = node.hasType() ? node.getTypeNode() : null;
		this.isFunction = false;
		this.variableName = node.getVariableName();
	}

	public HeaderNodeImpl(final ASTFunctionClauseNode node) {
		this.node = node;
		this.typedNode = node;
		this.isFunction = true;
		this.variableName = node.getVariableName();
	}

	/**
	 * A new header node with its value set to {@link NullLangObject}.
	 *
	 * @param variableName
	 *            Variable name.
	 */
	public HeaderNodeImpl(final String variableName, final Node prototype) {
		this.isFunction = false;
		this.variableName = variableName;
		this.node = new ASTNullNode(prototype);
		this.typedNode = null;
	}

	@Override
	public Node getNode() {
		return node;
	}

	@Override
	public boolean isFunction() {
		return isFunction;
	}

	@Override
	public Node getTypeNode() {
		if (typedNode != null)
			return typedNode;
		throw new FormExpressionException(CmnCnst.Error.GET_TYPE_NODE_CALLED_BUT_NO_TYPE_NODE_PRESENT);
	}

	@Override
	public boolean hasType() {
		return typedNode != null;
	}

	@Override
	public void resolveSource(final int source) throws IllegalVariableSourceResolutionException {
		if (this.source != EVariableSource.ID_UNRESOLVED && source != this.source)
			throw new IllegalVariableSourceResolutionException(this, source);
		this.source = source;
	}

	@Override
	public int getSource() {
		return source;
	}

	@Override
	public boolean isResolved() {
		return source >= 0;
	}

	@Override
	public String getVariableName() {
		return variableName;
	}

	@Override
	public String toString() {
		return NullUtil.messageFormat("HeaderNodeImpl({0},source={1})", variableName, source);
	}

	@Override
	public Node getDeclarationNode() {
		final Node parent = node.jjtGetParent();
		if (!isFunction && parent != null)
			return parent;
		return node;
	}
}