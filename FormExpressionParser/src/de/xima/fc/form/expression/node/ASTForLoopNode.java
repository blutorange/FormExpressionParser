package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTForLoopNode extends ANode {

	private static final long serialVersionUID = 1L;
	private String iteratingLoopVariable;

	@Nullable private String label;

	public ASTForLoopNode(@Nonnull final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}

	public void init(@Nullable final EMethod method, @Nullable final String iteratingLoopVariable, @Nullable final String label) throws ParseException {
		assertChildrenExactly(iteratingLoopVariable != null ? 2 : 4);
		super.init(method);
		this.iteratingLoopVariable = iteratingLoopVariable;
		this.label = label;
	}

	@Nullable
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
		return iteratingLoopVariable != null ? CmnCnst.Name.FOR_ITERATING_NODE : CmnCnst.Name.FOR_PLAIN_NODE;
	}

	@Nullable
	public String getLabel() {
		return label;
	}

}
