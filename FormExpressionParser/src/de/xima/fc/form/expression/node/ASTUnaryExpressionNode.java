package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTUnaryExpressionNode extends SimpleNode {
	private EMethod unaryMethod;

	public ASTUnaryExpressionNode(final int id) {
		super(id);
	}

	public final void init(final EMethod method, final EMethod unary) throws ParseException {
		assertChildrenExactly(1);
		siblingMethod = method;
		unaryMethod = unary;
	}

	@Override
	protected void additionalToStringFields(StringBuilder sb) {
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
