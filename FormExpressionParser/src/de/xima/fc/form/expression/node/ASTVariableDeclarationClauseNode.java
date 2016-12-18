package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnDataVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnVoidVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionVoidDataVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionVoidVoidVisitor;
import de.xima.fc.form.expression.iface.parse.IVariableTyped;
import de.xima.fc.form.expression.impl.variable.ELangObjectClass;

public class ASTVariableDeclarationClauseNode extends ASourceResolvableNode implements IVariableTyped {
	private static final long serialVersionUID = 1L;

	public ASTVariableDeclarationClauseNode(@Nonnull final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	@Override
	public void init(final EMethod method, final String variableName) throws ParseException {
		assertChildrenBetween(1,2);
		super.init(method, variableName);
	}

	@Override
	protected Node replacementOnChildRemoval(final int i) throws ArrayIndexOutOfBoundsException {
		if (i==0)
			return new ASTVariableTypeNode(jjtGetChild(0), ELangObjectClass.NULL);
		return null;
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionReturnDataVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}

	@Override
	public <R, E extends Throwable> R jjtAccept(final IFormExpressionReturnVoidVisitor<R, E> visitor) throws E {
		return visitor.visit(this);
	}

	@Override
	public <T, E extends Throwable> void jjtAccept(final IFormExpressionVoidDataVisitor<T, E> visitor, final T data) throws E {
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

	@Nonnull
	public Node getAssignmentNode() {
		return jjtGetChild(1);
	}
}