package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTBooleanNode extends SimpleNode {
	private static final long serialVersionUID = 1L;

	private boolean booleanValue;

	public ASTBooleanNode(@Nonnull final FormExpressionParser parser, final int id) {
		super(parser, id);
	}

	@Override
	protected void additionalToStringFields(@Nonnull final StringBuilder sb) {
		sb.append(booleanValue ? CmnCnst.ToString.BOOLEAN_TRUE : CmnCnst.ToString.BOOLEAN_FALSE).append(',');
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}

	public void init(@Nullable final EMethod method, final boolean b) throws ParseException {
		assertChildrenExactly(0);
		booleanValue = b;
		super.init(method);
	}

	public boolean getBooleanValue() {
		return booleanValue;
	}
}