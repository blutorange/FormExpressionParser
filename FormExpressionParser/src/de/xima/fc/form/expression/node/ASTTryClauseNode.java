package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTTryClauseNode extends SimpleNode {

	private String errorVariableName;

	public ASTTryClauseNode(final int nodeId) {
		super(nodeId);
	}

	@Override
	public <R, T> R jjtAccept(final IFormExpressionParserVisitor<R, T> visitor, final T data) throws EvaluationException {
		return visitor.visit(this, data);
	}

	public void init(final EMethod method, final String errorVariableName) throws ParseException {
		assertChildrenExactly(2);
		siblingMethod = method;
		this.errorVariableName = errorVariableName;
	}

	public String getErrorVariableName() {
		return errorVariableName;
	}


}
