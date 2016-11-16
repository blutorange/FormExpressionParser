package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTPostUnaryExpressionNode extends ANode {
	private static final long serialVersionUID = 1L;
	@Nonnull private EMethod unaryMethod = EMethod.PLUS_UNARY;

	public ASTPostUnaryExpressionNode(@Nonnull final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	public final void init(final EMethod method, @Nonnull final EMethod unary) throws ParseException {
		assertChildrenExactly(1);
		Preconditions.checkNotNull(unary);
		if (unary.isAssigning())
			assertChildrenAssignable(0, 1, CmnCnst.Name.SUFFIX_OPERATION);
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