package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTExceptionNode extends SimpleNode {
	public ASTExceptionNode(final int id) {
		super(id);
	}

	/**
	 * @param delimiter Character which delimits the string. " or '
	 */
	public void init(final EMethod method) throws ParseException {
		assertChildrenExactly(1);
		siblingMethod = method;
	}


	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}
}
