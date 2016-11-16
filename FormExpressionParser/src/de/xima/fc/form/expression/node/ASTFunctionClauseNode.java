package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.CmnCnst.Syntax;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTFunctionClauseNode extends ANode {
	private static final long serialVersionUID = 1L;

	@Nonnull private String functionName = CmnCnst.EMPTY_STRING;

	public ASTFunctionClauseNode(@Nonnull final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	@Override
	public void init(final EMethod method) throws ParseException {
		assertChildrenAtLeast(2);
		super.init(method);
		final ASTVariableNode var = getNthChildAs(0, ASTVariableNode.class);
		functionName = var.getScope() != null ? var.getScope() + Syntax.SCOPE_SEPARATOR + var.getName() : var.getName();
	}

	@Nonnull
	public String getFunctionName() {
		return functionName;
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}
}
