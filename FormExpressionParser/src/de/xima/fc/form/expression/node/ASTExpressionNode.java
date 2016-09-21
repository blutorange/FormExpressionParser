package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTExpressionNode extends SimpleNode {
	private EMethod unaryMethod;

	public ASTExpressionNode(final int id) {
		super(id);
	}

	public void init(final EMethod method) throws ParseException {
		assertChildrenAtLeast(1);
		siblingMethod = method;
	}

	public final void init(final EMethod method, final EMethod unaryMethod) throws ParseException {
		assertChildrenExactly(1);
		siblingMethod = method;
		this.unaryMethod = unaryMethod;
	}

	@Override
	public String toString() {
		if (children.length == 0) return "EmptyExpressionNode(" + siblingMethod + ")";
		if (children.length == 1)
			return "UnaryExpressionNode(" + String.valueOf(siblingMethod) + "," + String.valueOf(unaryMethod) + ")";
		return "BinaryExpressionNode(" + String.valueOf(siblingMethod) + ")";
	}

	public EMethod getUnaryMethod() {
		return unaryMethod;
	}

	@Override
	public <R, T> R jjtAccept(final IFormExpressionParserVisitor<R, T> visitor, final T data) throws EvaluationException {
		return visitor.visit(this, data);
	}
}
