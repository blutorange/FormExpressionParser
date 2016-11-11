package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTExceptionNode extends SimpleNode {
	private static final long serialVersionUID = 1L;
	public ASTExceptionNode(@Nonnull final FormExpressionParser parser, final int id) {
		super(parser, id);
	}

	/**
	 * @param delimiter Character which delimits the string. " or '
	 */
	@Override
	public void init(final EMethod method) throws ParseException {
		assertChildrenExactly(1);
		super.init(method);
	}


	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}
}
