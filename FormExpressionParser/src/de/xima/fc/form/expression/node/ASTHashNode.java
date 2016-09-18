package de.xima.fc.form.expression.node;

import java.util.ArrayList;
import java.util.List;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.HashLangObject;

public class ASTHashNode extends MySimpleNode {

	private Node[] childArray;

	public ASTHashNode(final int id) {
		super(id);
	}

	public ASTHashNode(final FormExpressionParser p, final int id) {
		super(p, id);
	}

	@Override
	public ALangObject evaluate(final IEvaluationContext ec) throws EvaluationException {
		final List<ALangObject> list = new ArrayList<ALangObject>(childArray.length);
		for (final Node n : childArray) list.add(n.evaluate(ec));
		return HashLangObject.create(list);
	}

	public void init() throws ParseException {
		childArray = getChildArray();
		if (childArray.length % 2 != 0) throw new ParseException("Children count odd: " + childArray.length);
	}
}