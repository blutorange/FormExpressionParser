package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.ELogLevel;
import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTLogNode extends SimpleNode {
	private static final long serialVersionUID = 1L;
	private ELogLevel logLevel;

	public ASTLogNode(final FormExpressionParser parser, final int id) {
		super(parser, id);
	}

	/**
	 * @param delimiter Character which delimits the string. " or '
	 */
	public void init(final EMethod method, final ELogLevel logLevel) throws ParseException {
		assertChildrenExactly(1);
		super.init(method);
		this.logLevel = logLevel;
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}


	@Override
	protected void additionalToStringFields(final StringBuilder sb) {
		sb.append(logLevel).append(',');
	}

	public ELogLevel getLogLevel() {
		return logLevel;
	}

}
