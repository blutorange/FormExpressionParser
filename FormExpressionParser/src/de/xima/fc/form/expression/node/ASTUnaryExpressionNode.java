package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
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
	public String toString() {
		return "UnaryExpressionNode(" + String.valueOf(siblingMethod) + "," + String.valueOf(unaryMethod) + ")";
	}

	public EMethod getUnaryMethod() {
		return unaryMethod;
	}

	@Override
	public <R, T> R jjtAccept(final IFormExpressionParserVisitor<R, T> visitor, final T data) throws EvaluationException {
		return visitor.visit(this, data);
	}
}