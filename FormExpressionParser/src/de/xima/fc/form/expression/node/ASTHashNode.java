package de.xima.fc.form.expression.node;

import java.util.ArrayList;
import java.util.List;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.error.EvaluationException;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.HashLangObject;

public class ASTHashNode extends MySimpleNode {

	private MySimpleNode[] value;

	public ASTHashNode(final int id) {
		super(id);
	}

	public ASTHashNode(final FormExpressionParser p, final int id) {
		super(p, id);
	}

	@Override
	public ALangObject evaluate(final IEvaluationContext ec) throws EvaluationException {
		final List<ALangObject> list = new ArrayList<ALangObject>(children.length);
		for (final MySimpleNode n : value) list.add(n.evaluate(ec));
		return HashLangObject.create(list);
	}

	public void init() throws ParseException {
		value = getChildArray();
		if (children.length % 2 != 0) throw new ParseException("children count odd");
	}
}