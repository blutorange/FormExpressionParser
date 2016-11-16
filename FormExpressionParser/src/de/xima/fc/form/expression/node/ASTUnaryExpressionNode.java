package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTUnaryExpressionNode extends ANode {
	private static final long serialVersionUID = 1L;

	public ASTUnaryExpressionNode(@Nonnull final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	@Nonnull
	private EMethod unaryMethod = EMethod.PLUS_UNARY;

	public final void init(@Nullable final EMethod method, @Nonnull final EMethod unary) throws ParseException {
		assertChildrenExactly(1);
		assertNonNull(unary, CmnCnst.Error.NULL_METHOD);
		if (unary.isAssigning())
			assertChildrenAssignable(0, 1, CmnCnst.NAME_PREFIX_OPERATION);
		super.init(method);
		unaryMethod = unary;
	}

	@Override
	protected void additionalToStringFields(final StringBuilder sb) {
		sb.append(unaryMethod).append(',');
	}

	@Nonnull
	public EMethod getUnaryMethod() {
		return unaryMethod;
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}
}
