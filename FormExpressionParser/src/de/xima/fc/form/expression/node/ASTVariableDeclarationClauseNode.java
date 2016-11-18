package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.visitor.IFormExpressionReturnDataVisitor;
import de.xima.fc.form.expression.visitor.IFormExpressionReturnVoidVisitor;
import de.xima.fc.form.expression.visitor.IFormExpressionVoidDataVisitor;
import de.xima.fc.form.expression.visitor.IFormExpressionVoidVoidVisitor;

public class ASTVariableDeclarationClauseNode extends ANode {
	private static final long serialVersionUID = 1L;
	@Nonnull
	private String variableName = CmnCnst.NonnullConstant.EMPTY_STRING;

	public ASTVariableDeclarationClauseNode(@Nonnull final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	public void init(final EMethod method, final String variableName) throws ParseException {
		assertNonNull(variableName, CmnCnst.Error.VARIABLE_NODE_NULL_NAME);
		assertChildrenAtMost(1);
		super.init(method);
		if (variableName != null)
			this.variableName = variableName;
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

	@Nonnull
	public String getVariableName() {
		return variableName;
	}

	@Override
	protected void additionalToStringFields(@Nonnull final StringBuilder sb) {
		sb.append(variableName).append(',');
	}
}
