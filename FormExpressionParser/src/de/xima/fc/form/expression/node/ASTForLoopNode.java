package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTForLoopNode extends SimpleNode {

	private static final long serialVersionUID = 1L;
	private String iteratingLoopVariable;

	private String label;

	public ASTForLoopNode(final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}

	public void init(final EMethod method, final String iteratingLoopVariable, final String label) throws ParseException {
		assertChildrenExactly(iteratingLoopVariable != null ? 2 : 4);
		super.init(method);
		this.iteratingLoopVariable = iteratingLoopVariable;
		this.label = label;
	}

	public String getIteratingLoopVariable() {
		return iteratingLoopVariable;
	}

	@Override
	protected void additionalToStringFields(final StringBuilder sb) {
		sb.append(iteratingLoopVariable).append(',');
		sb.append(label).append(',');
	}

	@Override
	protected String nodeName() {
		return iteratingLoopVariable != null ? CmnCnst.NAME_FOR_ITERATING_NODE : CmnCnst.NAME_FOR_PLAIN_NODE;
	}

	public String getLabel() {
		return label;
	}

}
