package de.xima.fc.form.expression.impl.variable;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import de.xima.fc.form.expression.enums.EVariableSource;
import de.xima.fc.form.expression.exception.parse.IllegalVariableSourceResolutionException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.iface.parse.IHeaderNode;
import de.xima.fc.form.expression.node.ASTFunctionClauseNode;
import de.xima.fc.form.expression.node.ASTNullNode;
import de.xima.fc.form.expression.node.ASTVariableDeclarationClauseNode;
import de.xima.fc.form.expression.object.NullLangObject;

@Immutable
public class HeaderNodeImpl implements IHeaderNode {
	private static final long serialVersionUID = 1L;
	
	@Nonnull
	private final Node node;
	@Nonnull
	private final String variableName;
	private final boolean isFunction;
	private final boolean hasNode;
	private int source = EVariableSource.ID_UNRESOLVED;
	
	public HeaderNodeImpl(@Nonnull final ASTVariableDeclarationClauseNode node) {
		final Node assignmentNode = node.getAssignmentNode();
		this.node = assignmentNode != null ? assignmentNode : new ASTNullNode(node);
		this.isFunction = false;
		this.variableName = node.getVariableName();
		this.hasNode = true;
	}
	
	public HeaderNodeImpl(@Nonnull final ASTFunctionClauseNode node) {
		this.node = node;
		this.isFunction = true;
		this.variableName = node.getVariableName();
		this.hasNode = true;
	}

	/**
	 * A new header node with its value set to {@link NullLangObject}.
	 * @param variableName Variable name.
	 */
	public HeaderNodeImpl(@Nonnull final String variableName, @Nonnull final Node prototype) {
		this.isFunction = false;
		this.variableName = variableName;
		this.node = new ASTNullNode(prototype);
		this.hasNode = false;
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
	public boolean hasNode() {
		return hasNode;
	}
	
	@Override
	public String toString() {
		return String.format("HeaderNodeImpl(%s,source=%d)", variableName, source);
	}
}