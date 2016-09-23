package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.ELogLevel;
import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTLogNode extends SimpleNode {
	private ELogLevel logLevel;
	
	public ASTLogNode(final int id) {
		super(id);
	}

	/**
	 * @param delimiter Character which delimits the string. " or '
	 */
	public void init(final EMethod method, ELogLevel logLevel) throws ParseException {
		assertChildrenExactly(1);
		siblingMethod = method;
		this.logLevel = logLevel;
	}

	@Override
	public <R, T> R jjtAccept(final IFormExpressionParserVisitor<R, T> visitor, final T data) throws EvaluationException {
		return visitor.visit(this, data);
	}

	@Override
	public String toString() {
		return "LogNode(" + logLevel + ")";
	}
	
	public ELogLevel getLogLevel() {
		return logLevel;
	}

}
