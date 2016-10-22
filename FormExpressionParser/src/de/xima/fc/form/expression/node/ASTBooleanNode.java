package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTBooleanNode extends SimpleNode {

	private boolean booleanValue;

	public ASTBooleanNode(FormExpressionParser parser, final int id) {
		super(parser, id);
	}

	@Override
	protected void additionalToStringFields(final StringBuilder sb) {
		sb.append((booleanValue ? "true)" : "false)")).append(",");
	}
	
	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
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