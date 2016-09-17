package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.NullLangObject;

public class ASTPlainNode extends MySimpleNode {
	private MySimpleNode child;

	public ASTPlainNode(final int id) {
		super(id);
	}

	public ASTPlainNode(final FormExpressionParser p, final int id) {
		super(p, id);
	}

	@Override
	public ALangObject evaluate(final IEvaluationContext ec) throws EvaluationException {
		return child == null ? NullLangObject.getInstance() : child.evaluate(ec);
	}

	public void init() throws ParseException {
		child = getSingleChild();
	}
}
