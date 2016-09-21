package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTForLoopNode extends SimpleNode {

	private String iteratingLoopVariable;

	public ASTForLoopNode(final int nodeId) {
		super(nodeId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public <R, T> R jjtAccept(final IFormExpressionParserVisitor<R, T> visitor, final T data) throws EvaluationException {
		return visitor.visit(this, data);
	}

	public void init(final String iteratingLoopVariable) throws ParseException {
		this.iteratingLoopVariable = iteratingLoopVariable;
	}

	public String getIteratingLoopVariable() {
		return iteratingLoopVariable;
	}

	@Override
	public String toString() {
		if (iteratingLoopVariable != null) return "ForIteratingLoopNode(" + iteratingLoopVariable + ")";
		return "ForPlainLoopNode";
	}

}
