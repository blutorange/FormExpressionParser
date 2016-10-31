package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTExpressionNode extends SimpleNode {
	private static final long serialVersionUID = 1L;
	public ASTExpressionNode(final FormExpressionParser parser, final int id) {
		super(parser, id);
	}

	@Override
	public void init(final EMethod method) throws ParseException {
		assertChildrenAtLeast(1);
		super.init(method);
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}
}
