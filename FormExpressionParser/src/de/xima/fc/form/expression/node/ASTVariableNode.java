package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTVariableNode extends SimpleNode {

	private String name;
    private String scope;
	
	public ASTVariableNode(final int id) {
		super(id);
	}

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
		if (name == null) throw new ParseException("Name is null. This is likely an error with the parser. Contact support.");
		siblingMethod = method;
		this.name =  name;
		this.scope = scope;
	}

	@Override
	protected void additionalToStringFields(StringBuilder sb) {
		sb.append(scope).append(",").append(name).append(",");
	}

	@Override
	public <R, T, E extends Throwable> R jjtAccept(final IFormExpressionParserVisitor<R, T, E> visitor, final T data) throws E {
		return visitor.visit(this, data);
	}
}