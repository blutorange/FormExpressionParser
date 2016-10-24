package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTTryClauseNode extends SimpleNode {

	public ASTTryClauseNode(final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	private String errorVariableName;


	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}

	public void init(final EMethod method, final String errorVariableName) throws ParseException {
		assertChildrenExactly(2);
		super.init(method);
		this.errorVariableName = errorVariableName;
	}

	public String getErrorVariableName() {
		return errorVariableName;
	}


}
