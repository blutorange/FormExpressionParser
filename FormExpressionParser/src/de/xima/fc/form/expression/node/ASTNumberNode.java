package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnDataVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionReturnVoidVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionVoidDataVisitor;
import de.xima.fc.form.expression.iface.evaluate.IFormExpressionVoidVoidVisitor;
import de.xima.fc.form.expression.util.CmnCnst;
import de.xima.fc.form.expression.util.NullUtil;

public class ASTNumberNode extends ANode {
	private static final long serialVersionUID = 1L;
	private double doubleValue;

	public ASTNumberNode(@Nonnull final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	/**
	 * @param string String representing the number.
	 * @param isInt Whether it is an integer or float.
	 */
	public void init(@Nullable final EMethod method, @Nullable final String string, final boolean isInt) throws ParseException {
		assertChildrenExactly(0);
		try {
			doubleValue = Double.parseDouble(string);
		}
		catch (final NumberFormatException e) {
			throw new ParseException(NullUtil.messageFormat(CmnCnst.Error.NODE_INVALID_NUMBER, string, new Integer(getBeginLine()), new Integer(getBeginColumn()), e.getMessage()));
		}
		super.init(method);
	}

	@Override
	protected final Node replacementOnChildRemoval(final int i) throws ArrayIndexOutOfBoundsException {
		return null;
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

	@Override
	protected void additionalToStringFields(final StringBuilder sb) {
		sb.append(doubleValue).append(',');
	}

	public double getDoubleValue() {
		return doubleValue;
	}
}
