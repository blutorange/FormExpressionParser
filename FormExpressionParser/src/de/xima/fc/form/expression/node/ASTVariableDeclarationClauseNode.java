package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.eclipse.jdt.annotation.NonNullByDefault;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.parse.SemanticsException;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnDataVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnVoidVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionVoidDataVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionVoidVoidVisitor;
import de.xima.fc.form.expression.iface.evaluate.ILangObjectClass;
import de.xima.fc.form.expression.iface.parse.IHeaderNode;
import de.xima.fc.form.expression.iface.parse.IVariableTyped;
import de.xima.fc.form.expression.impl.variable.ELangObjectClass;
import de.xima.fc.form.expression.util.CmnCnst;

@NonNullByDefault
public class ASTVariableDeclarationClauseNode extends ASourceResolvableNode implements IVariableTyped, IHeaderNode {
	private static final long serialVersionUID = 1L;

	public ASTVariableDeclarationClauseNode(final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	@Override
	public void init(@Nullable final EMethod method, final String variableName) throws ParseException {
		assertChildOfType(0, ASTVariableTypeNode.class);
		assertChildrenBetween(1, 2);
		super.init(method, variableName);
	}

	@Nullable
	@Override
	protected Node replacementOnChildRemoval(final int i) throws ArrayIndexOutOfBoundsException {
		if (i == 0)
			return new ASTVariableTypeNode(jjtGetChild(0), ELangObjectClass.NULL);
		return null;
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionReturnDataVisitor<R, T, E> visitor,
			final T data) throws E {
		return visitor.visit(this, data);
	}

	@Override
	public <R, E extends Throwable> R jjtAccept(final IFormExpressionReturnVoidVisitor<R, E> visitor) throws E {
		return visitor.visit(this);
	}

	@Override
	public <T, E extends Throwable> void jjtAccept(final IFormExpressionVoidDataVisitor<T, E> visitor, final T data)
			throws E {
		visitor.visit(this, data);
	}

	@Override
	public <E extends Throwable> void jjtAccept(final IFormExpressionVoidVoidVisitor<E> visitor) throws E {
		visitor.visit(this);
	}

	@Override
	protected void additionalToStringFields(@Nonnull final StringBuilder sb) {
		super.additionalToStringFields(sb);
		sb.append(getVariableName()).append(',');
	}

	public boolean hasAssignment() {
		return jjtGetNumChildren() == 2;
	}

	@Override
	public boolean hasType() {
		return true;
	}

	@Override
	public Node getTypeNode() {
		return jjtGetChild(0);
	}

	public Node getAssignmentNode() {
		return jjtGetChild(1);
	}

	public void addAssignmentNode(final Node assignmentNode) throws SemanticsException {
		if (hasAssignment())
			throw new SemanticsException(CmnCnst.Error.ASSIGNMENT_NODE_EXISTS_ALREADY, this);
		jjtAddChild(assignmentNode, 1);
	}

	public ILangObjectClass getLangObjectClass() {
		if (!hasType())
			return ELangObjectClass.OBJECT;
		final ASTVariableTypeNode node = getNthChildAsOrNull(0, ASTVariableTypeNode.class);
		return node != null ? node.getLangObjectClass() : ELangObjectClass.OBJECT;
	}

	@Override
	public Node getHeaderValueNode() {
		return hasAssignment() ? getAssignmentNode() : new ASTNullNode(this);
	}

	@Override
	public boolean isFunction() {
		return false;
	}

	@Override
	public Node getHeaderDeclarationNode() {
		return this;
	}
}