package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTAssignmentExpressionNode extends ANode {
	private static final long serialVersionUID = 1L;
	public ASTAssignmentExpressionNode(@Nonnull final FormExpressionParser parser, final int id) {
		super(parser, id);
	}

	@Override
	public void init(final EMethod method) throws ParseException {
		assertChildrenAtLeast(2);
		assertChildrenAssignable(0, jjtGetNumChildren()-1, CmnCnst.NAME_ASSIGNMENT);
		super.init(method);
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}
}
