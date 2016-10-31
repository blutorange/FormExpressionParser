package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTContinueClauseNode extends SimpleNode {
	private static final long serialVersionUID = 1L;
	private String label;

	public ASTContinueClauseNode(final FormExpressionParser parser, final int id) {
		super(parser, id);
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}

	public void init(final EMethod method, final String label) throws ParseException {
		assertChildrenExactly(0);
		super.init(method);
		this.label = label;
	}

	@Override
	protected void additionalToStringFields(final StringBuilder sb) {
		sb.append(label).append(',');
	}

	public String getLabel() {
		return label;
	}
}
