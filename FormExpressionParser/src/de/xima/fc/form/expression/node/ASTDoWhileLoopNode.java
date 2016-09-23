package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTDoWhileLoopNode extends SimpleNode {

	private String label;
	
	public ASTDoWhileLoopNode(final int nodeId) {
		super(nodeId);
	}

	@Override
	public <R, T> R jjtAccept(final IFormExpressionParserVisitor<R, T> visitor, final T data) throws EvaluationException {
		return visitor.visit(this, data);
	}

	public void init(final EMethod method) throws ParseException {
		assertChildrenExactly(2);
		siblingMethod = method;
	}
	
	public String getLabel() {
		return label;
	}
}