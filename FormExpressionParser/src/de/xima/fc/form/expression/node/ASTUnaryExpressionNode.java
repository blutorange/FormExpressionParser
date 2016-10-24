package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTUnaryExpressionNode extends SimpleNode {

	public ASTUnaryExpressionNode(final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	private EMethod unaryMethod;

	public final void init(final EMethod method, final EMethod unary) throws ParseException {
		assertChildrenExactly(1);
		if (unary != null && unary.isAssigning())
			assertChildrenAssignable(0, 1, "prefix operation");
		super.init(method);
		unaryMethod = unary;
	}

	@Override
	protected void additionalToStringFields(final StringBuilder sb) {
		sb.append(unaryMethod).append(",");
	}

	public EMethod getUnaryMethod() {
		return unaryMethod;
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}
}
