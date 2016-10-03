package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTForLoopNode extends SimpleNode {

	private String iteratingLoopVariable;

	private String label;
	
	public ASTForLoopNode(final int nodeId) {
		super(nodeId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}

	public void init(final EMethod method, final String iteratingLoopVariable) throws ParseException {
		assertChildrenExactly(iteratingLoopVariable != null ? 2 : 4);
		siblingMethod = method;
		this.iteratingLoopVariable = iteratingLoopVariable;
	}

	public String getIteratingLoopVariable() {
		return iteratingLoopVariable;
	}

	@Override
	protected void additionalToStringFields(StringBuilder sb) {
		if (iteratingLoopVariable != null) sb.append(iteratingLoopVariable).append(",");
	}
	
	@Override
	protected String nodeName() {
		return iteratingLoopVariable != null ? "ForIteratingLoopNode" : "ForPlainLoopNode";
	}
	
	public String getLabel() {
		return label;
	}

}
