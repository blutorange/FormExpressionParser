package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTBooleanNode extends SimpleNode {

	private boolean booleanValue;

	public ASTBooleanNode(final int id) {
		super(id);
	}

	@Override
	protected void additionalToStringFields(final StringBuilder sb) {
		sb.append((booleanValue ? "true)" : "false)")).append(",");
	}

	@Override
	public <R, T> R jjtAccept(final IFormExpressionParserVisitor<R, T> visitor, final T data) throws EvaluationException {
		return visitor.visit(this, data);
	}

	public void init(final EMethod method, final boolean b) throws ParseException {
		booleanValue = b;
		siblingMethod = method;
	}

	public boolean getBooleanValue() {
		return booleanValue;
	}
}