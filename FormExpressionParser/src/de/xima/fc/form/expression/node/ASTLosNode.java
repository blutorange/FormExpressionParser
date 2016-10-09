package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTLosNode extends SimpleNode {

	private String text;
	private boolean hasOpen;
	private boolean hasClose;

	public ASTLosNode(final int nodeId) {
		super(nodeId);
	}

	public void init(final EMethod method, final String text, final String open, final boolean hasClose) throws ParseException {
		assertChildrenExactly(0);
		siblingMethod = method;
		this.text = text;
		this.hasOpen = open != null;
		this.hasClose = hasClose;
		if (open != null) this.embedment = open;
	}

	public String getText() {
		return text;
	}

	public boolean isHasOpen() {
		return hasOpen;
	}

	public boolean isHasClose() {
		return hasClose;
	}

	public boolean isHasText() {
		return text != null;
	}

	@Override
	protected void additionalToStringFields(final StringBuilder sb) {
		sb.append(hasOpen)
		.append(',')
		.append(hasClose)
		.append(',')
		.append(text);
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}


}
