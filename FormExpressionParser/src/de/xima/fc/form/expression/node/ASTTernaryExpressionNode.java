package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTTernaryExpressionNode extends SimpleNode {


	public ASTTernaryExpressionNode(FormExpressionParser parser, int nodeId) {
		super(parser, nodeId);
	}

	public void init(final EMethod method) throws ParseException {
		assertChildrenExactly(3);
		this.siblingMethod = method;
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}
}
