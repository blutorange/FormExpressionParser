package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTHashNode extends MySimpleNode {

	private Node[] childArray;

	public ASTHashNode(final int id) {
		super(id);
	}

	public ASTHashNode(final FormExpressionParser p, final int id) {
		super(p, id);
	}

	@Override
	public <R, T> R jjtAccept(final IFormExpressionParserVisitor<R, T> visitor, final T data) throws EvaluationException {
		return visitor.visit(this, data);
	}

	public void init() throws ParseException {
		childArray = getChildArray();
		if (childArray.length % 2 != 0) throw new ParseException("Children count odd: " + childArray.length);
	}
}