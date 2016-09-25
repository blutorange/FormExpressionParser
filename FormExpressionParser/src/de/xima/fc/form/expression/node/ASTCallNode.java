package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTCallNode extends SimpleNode {

	public ASTCallNode(final int id) {
		super(id);
	}

	public void init(final EMethod method) throws ParseException {
		siblingMethod = method;
	}

	@Override
	public String toString() {
		return "CallNode(" + siblingMethod + ")";
	}

	@Override
	public <R, T> R jjtAccept(final IFormExpressionParserVisitor<R, T> visitor, final T data) throws EvaluationException {
		return visitor.visit(this, data);
	}
}