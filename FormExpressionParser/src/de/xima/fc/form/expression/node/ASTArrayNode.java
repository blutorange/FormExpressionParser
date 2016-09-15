package de.xima.fc.form.expression.node;

import java.util.ArrayList;
import java.util.List;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.error.EvaluationException;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ArrayLangObject;

public class ASTArrayNode extends MySimpleNode {

	private MySimpleNode[] value;

	public ASTArrayNode(final int id) {
		super(id);
	}

	public ASTArrayNode(final FormExpressionParser p, final int id) {
		super(p, id);
	}

	@Override
	public ALangObject evaluate(final IEvaluationContext ec) throws EvaluationException {
		final List<ALangObject> list = new ArrayList<ALangObject>(children.length);
		for (final MySimpleNode n : value) list.add(n.evaluate(ec));
		return ArrayLangObject.create(list);
	}

	public void init() throws ParseException {
		value = getChildArray();
	}
}