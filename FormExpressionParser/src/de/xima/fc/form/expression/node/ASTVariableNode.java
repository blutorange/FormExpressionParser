package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTVariableNode extends SimpleNode {

	public ASTVariableNode(final FormExpressionParser parser, final int nodeId) {
		super(parser, nodeId);
	}

	private String name;
	private String scope;

	public String getName() {
		return name;
	}

	public String getScope() {
		return scope;
	}

	public boolean hasScope() {
		return scope != null;
	}

	public void init(final EMethod method, final String scope, final String name) throws ParseException {
		assertChildrenAtMost(1);
		if (name == null) throw new ParseException("Name is null. This is likely an error with the parser. Contact support.");
		super.init(method);
		this.name =  name;
		this.scope = scope;
	}

	@Override
	protected void additionalToStringFields(final StringBuilder sb) {
		sb.append(scope).append(",").append(name).append(",");
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}
}