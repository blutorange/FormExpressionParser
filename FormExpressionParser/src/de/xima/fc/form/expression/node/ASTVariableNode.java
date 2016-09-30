package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
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
		if (name == null) throw new ParseException("name is null");
		siblingMethod = method;
		this.name =  name;
		this.scope = scope;
	}

	@Override
	public String toString() {
		return "VariableNode(" + siblingMethod + "," + scope + "," + name + ")";
	}

	@Override
	public <R, T> R jjtAccept(final IFormExpressionParserVisitor<R, T> visitor, final T data) throws EvaluationException {
		return visitor.visit(this, data);
	}
}