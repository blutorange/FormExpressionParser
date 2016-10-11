package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTComparisonExpressionNode extends SimpleNode {

	public ASTComparisonExpressionNode(final int nodeId) {
		super(nodeId);
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}

	public void init(final EMethod method) throws ParseException {
		assertChildrenAtLeast(2);
		this.siblingMethod = method;
	}
}
