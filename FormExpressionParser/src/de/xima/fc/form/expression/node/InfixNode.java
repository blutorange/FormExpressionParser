package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.util.InfixValue;

public abstract class InfixNode extends MySimpleNode {

	private InfixValue infixValue;

	public InfixNode(final int id) {
		super(id);
	}

	public InfixNode(final FormExpressionParser p, final int id) {
		super(p, id);
	}

	public MySimpleNode getLeft() {
		return infixValue.left;
	}

	public MySimpleNode getRight() {
		return infixValue.right;
	}

	public InfixValue getInfixValue() {
		return infixValue;
	}

	public void init(final Node ast) throws ParseException {
		if (ast.jjtGetNumChildren() != 2)
			throw new ParseException("node does not contain 2 children: " + ast.jjtGetNumChildren());
		final Node left = ast.jjtGetChild(0);
		final Node right = ast.jjtGetChild(1);
		if (!(left instanceof MySimpleNode))
			throw new ParseException("node left not the correct type: " + left.getClass());
		if (!(right instanceof MySimpleNode))
			throw new ParseException("node right not the correct type: " + left.getClass());
		infixValue = new InfixValue((MySimpleNode) left, (MySimpleNode) right);
	}

}
