package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTLosNode extends SimpleNode {

	private String text;
	private String embedmentType;
	private boolean hasClose;

	public ASTLosNode(final int nodeId) {
		super(nodeId);
	}

	public void init(final EMethod method, final String embedmentType, final String text, final boolean hasClose) throws ParseException {
		assertChildrenExactly(0);
		siblingMethod = method;
		this.text = text;
		this.embedmentType = embedmentType;
		this.hasClose = hasClose;
	}

	public String getText() {
		return text;
	}

	public String getEmbedmentType() {
		return embedmentType;
	}

	public boolean isHasOpen() {
		return embedmentType != null;
	}

	public boolean isHasClose() {
		return hasClose;
	}

	public boolean isHasText() {
		return text != null;
	}

	@Override
	protected void additionalToStringFields(final StringBuilder sb) {
		sb.append(embedmentType).append(',').append(hasClose).append(',').append(text).append(',');
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}


}
