package de.xima.fc.form.expression.node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.xima.fc.form.expression.enums.ELogLevel;
import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionReturnDataVisitor;
import de.xima.fc.form.expression.visitor.IFormExpressionReturnVoidVisitor;
import de.xima.fc.form.expression.visitor.IFormExpressionVoidDataVisitor;
import de.xima.fc.form.expression.visitor.IFormExpressionVoidVoidVisitor;

public class ASTLogNode extends ANode {
	private static final long serialVersionUID = 1L;
	@Nonnull
	private ELogLevel logLevel = ELogLevel.DEBUG;

	public ASTLogNode(@Nonnull final FormExpressionParser parser, final int id) {
		super(parser, id);
	}

	/**
	 * @param delimiter Character which delimits the string. " or '
	 */
	public void init(@Nullable final EMethod method, @Nonnull final ELogLevel logLevel) throws ParseException {
		assertChildrenExactly(1);
		super.init(method);
		this.logLevel = logLevel;
	}
	
	@Override
	protected final Node replacementOnChildRemoval(final int i) throws ArrayIndexOutOfBoundsException {
		return emptyStringNode();
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
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionReturnDataVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}


	@Override
	protected void additionalToStringFields(final StringBuilder sb) {
		sb.append(logLevel).append(',');
	}

	@Nonnull
	public ELogLevel getLogLevel() {
		return logLevel;
	}

	@Nonnull
	public Node getLogMessageNode() {
		return jjtGetChild(0);
	}

}
