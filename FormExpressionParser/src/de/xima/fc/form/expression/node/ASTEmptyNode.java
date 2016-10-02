package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTEmptyNode extends SimpleNode {

	public ASTEmptyNode(int nodeId) {
		super(nodeId);
	}

	@Override
	public <R, T> R jjtAccept(IFormExpressionParserVisitor<R, T> visitor, T data) throws EvaluationException {
		return visitor.visit(this, data);
	}

	public void init(EMethod method) throws ParseException {
		assertChildrenExactly(0);
		siblingMethod = method;
	}
}
