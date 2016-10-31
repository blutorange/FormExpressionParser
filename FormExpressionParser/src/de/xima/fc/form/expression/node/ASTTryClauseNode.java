package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTTryClauseNode extends SimpleNode {
	private static final long serialVersionUID = 1L;

	public ASTTryClauseNode(final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	@Nonnull private String errorVariableName = CmnCnst.EMPTY_STRING;


	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}

	public void init(final EMethod method, final String errorVariableName) throws ParseException {
		assertChildrenExactly(2);
		super.init(method);
		if (errorVariableName == null)
			throw new ParseException(CmnCnst.Error.NULL_ERROR_VARIABLE_NAME);
		this.errorVariableName = errorVariableName;
	}

	@Nonnull
	public String getErrorVariableName() {
		return errorVariableName;
	}
}
