package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTAssignmentExpressionNode extends SimpleNode {
	public ASTAssignmentExpressionNode(final FormExpressionParser parser, final int id) {
		super(parser, id);
	}

	@Override
	public void init(final EMethod method) throws ParseException {
		assertChildrenAtLeast(2);
		assertChildrenAssignable(0, children.length-1, "assignment");
		super.init(method);
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}
}
