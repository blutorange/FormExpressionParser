package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTIfClauseNode extends SimpleNode {

	private boolean hasElse;

	public ASTIfClauseNode(final int nodeId) {
		super(nodeId);
	}

	@Override
	public <R, T> R jjtAccept(final IFormExpressionParserVisitor<R, T> visitor, final T data) throws EvaluationException {
		return visitor.visit(this, data);
	}

	public void init(final boolean hasElse) throws ParseException {
		this.hasElse = hasElse;
	}

	public boolean isHasElse() {
		return hasElse;
	}
}
