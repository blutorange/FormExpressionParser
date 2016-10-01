package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTFunctionClauseNode extends SimpleNode {

	private String functionName;

	public ASTFunctionClauseNode(final int nodeId) {
		super(nodeId);
	}

	public void init(final EMethod method) throws ParseException {
		assertChildrenAtLeast(2);
		siblingMethod = method;
		final ASTVariableNode var = getNthChildAs(0, ASTVariableNode.class);
		functionName = var.getScope() != null ? var.getScope() + "::" + var.getName() : var.getName();
	}

	public String getFunctionName() {
		return functionName;
	}

	@Override
	public <R, T> R jjtAccept(final IFormExpressionParserVisitor<R, T> visitor, final T data) throws EvaluationException {
		return visitor.visit(this, data);
	}
}
