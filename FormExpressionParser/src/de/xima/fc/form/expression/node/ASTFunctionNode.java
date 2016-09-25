package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTFunctionNode extends SimpleNode {

	public ASTFunctionNode(int nodeId) {
		super(nodeId);
	}
	
	public void init(EMethod method) throws ParseException {
		assertChildrenExactly(2);
		siblingMethod = method;
	}

	@Override
	public <R, T> R jjtAccept(IFormExpressionParserVisitor<R, T> visitor, T data) throws EvaluationException {
		return visitor.visit(this, data);
	}
}
