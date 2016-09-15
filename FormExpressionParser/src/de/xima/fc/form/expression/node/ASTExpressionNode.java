package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.error.EvaluationException;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.object.ALangObject;

public class ASTExpressionNode extends MySimpleNode {
	private MySimpleNode child;

	public ASTExpressionNode(final int id) {
		super(id);
	}

	public ASTExpressionNode(final FormExpressionParser p, final int id) {
		super(p, id);
	}


	@Override
	public ALangObject evaluate(final IEvaluationContext ec) throws EvaluationException {
		return child.evaluate(ec);
	}

	public void init() throws ParseException {
		child = getSingleChild();
	}
}
