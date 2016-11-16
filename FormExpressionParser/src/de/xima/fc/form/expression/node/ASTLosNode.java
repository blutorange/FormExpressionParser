package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTLosNode extends ANode {
	private static final long serialVersionUID = 1L;

	@Nullable private String text;
	@Nullable private String open;
	private boolean hasClose;

	public ASTLosNode(@Nonnull final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	public void init(@Nullable final EMethod method, @Nullable final String text, @Nullable final String open, final boolean hasClose) throws ParseException {
		assertChildrenExactly(0);
		super.init(method);
		this.text = text;
		this.open = open;
		this.hasClose = hasClose;
	}

	@Nonnull
	public String getText() {
		return text != null ? text : CmnCnst.EMPTY_STRING;
	}

	public boolean isHasOpen() {
		return open != null;
	}

	public boolean isHasClose() {
		return hasClose;
	}

	public boolean isHasText() {
		return text != null;
	}

	@Nonnull
	public String getOpen() {
		return open != null ? open : CmnCnst.EMPTY_STRING;
	}

	@Override
	protected void additionalToStringFields(final StringBuilder sb) {
		sb.append(open)
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
