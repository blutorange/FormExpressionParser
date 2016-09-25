package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTExpressionNode extends SimpleNode {
	public ASTExpressionNode(final int id) {
		super(id);
	}

	public void init(final EMethod method) throws ParseException {
		assertChildrenAtLeast(1);
		siblingMethod = method;
	}

	@Override
	public String toString() {
		if (children.length == 0) return "EmptyExpressionNode(" + siblingMethod + ")";
		return "ExpressionNode(" + String.valueOf(siblingMethod) + ")";
	}


	@Override
	public <R, T> R jjtAccept(final IFormExpressionParserVisitor<R, T> visitor, final T data) throws EvaluationException {
		return visitor.visit(this, data);
	}
}
