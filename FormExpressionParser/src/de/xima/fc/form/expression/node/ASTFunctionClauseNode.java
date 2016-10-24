package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTFunctionClauseNode extends SimpleNode {

	private String functionName;

	public ASTFunctionClauseNode(final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	@Override
	public void init(final EMethod method) throws ParseException {
		assertChildrenAtLeast(2);
		super.init(method);
		final ASTVariableNode var = getNthChildAs(0, ASTVariableNode.class);
		functionName = var.getScope() != null ? var.getScope() + "::" + var.getName() : var.getName();
	}

	public String getFunctionName() {
		return functionName;
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}
}
