package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTTemplateNode extends SimpleNode {

	public ASTTemplateNode(final int nodeId) {
		super(nodeId);
	}

	public void init(final EMethod method) {
		siblingMethod = method;
	}

	@Override
	public <R, T> R jjtAccept(final IFormExpressionParserVisitor<R, T> visitor, final T data) throws EvaluationException {
		return visitor.visit(this, data);
	}
}
