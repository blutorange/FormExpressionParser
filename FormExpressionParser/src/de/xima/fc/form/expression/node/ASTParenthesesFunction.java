package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.enums.EChainType;
import de.xima.fc.form.expression.enums.EMethod;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTParenthesesFunction extends AFunctionCallNode {
	private String name;

	public ASTParenthesesFunction(final int id) {
		super(id, EChainType.INSTANCE_METHOD);
	}

	@Override
	public void init(final EMethod method, final String name) throws ParseException {
		if (name == null) throw new ParseException("Name is null");
		siblingMethod = method;
		this.name = name;
	}

	@Override
	public String toString() {
		return "ParenthesesFunction(" + name + ")";
	}


	@Override
	public String getMethodName() {
		return name;
	}

	@Override
	public <R, T> R jjtAccept(final IFormExpressionParserVisitor<R, T> visitor, final T data) throws EvaluationException {
		return visitor.visit(this, data);
	}

}