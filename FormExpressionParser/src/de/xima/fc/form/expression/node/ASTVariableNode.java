package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTVariableNode extends SimpleNode {

	private String name;

	public ASTVariableNode(final int id) {
		super(id);
	}

	public String getMethodName() {
		return name;
	}

	public void init(final EMethod method, final String name) throws ParseException {
		if (name == null) throw new ParseException("name is null");
		siblingMethod = method;
		this.name =  name;
	}

	@Override
	public String toString() {
		return "VariableNode(" + siblingMethod + "," + name + ")";
	}

	@Override
	public <R, T> R jjtAccept(final IFormExpressionParserVisitor<R, T> visitor, final T data) throws EvaluationException {
		return visitor.visit(this, data);
	}
}