package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.visitor.IFormExpressionReturnDataVisitor;
import de.xima.fc.form.expression.visitor.IFormExpressionReturnVoidVisitor;
import de.xima.fc.form.expression.visitor.IFormExpressionVoidDataVisitor;
import de.xima.fc.form.expression.visitor.IFormExpressionVoidVoidVisitor;

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

	@Override
	protected Node replacementOnChildRemoval(final int i) throws ArrayIndexOutOfBoundsException {
		return null;
	}

	@Nonnull
	public String getText() {
		return text != null ? text : CmnCnst.NonnullConstant.STRING_EMPTY;
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
		return open != null ? open : CmnCnst.NonnullConstant.STRING_EMPTY;
	}

	@Override
	protected void additionalToStringFields(final StringBuilder sb) {
		sb.append(open)
		.append(',')
		.append(hasClose)
		.append(',')
		.append(text)
		.append(',');
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionReturnDataVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}

	@Override
	public <R, E extends Throwable> R jjtAccept(final IFormExpressionReturnVoidVisitor<R, E> visitor) throws E {
		return visitor.visit(this);
	}

	@Override
	public <T, E extends Throwable> void jjtAccept(final IFormExpressionVoidDataVisitor<T, E> visitor, final T data) throws E {
		visitor.visit(this, data);
	}

	@Override
	public <E extends Throwable> void jjtAccept(final IFormExpressionVoidVoidVisitor<E> visitor) throws E {
		visitor.visit(this);
	}


}
