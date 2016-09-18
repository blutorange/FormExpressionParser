package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTArrayNode extends MySimpleNode {
	public ASTArrayNode(final int id) {
		super(id);
	}

	public ASTArrayNode(final FormExpressionParser p, final int id) {
		super(p, id);
	}

	//TODO remove this
	//	public ALangObject evaluate(final IEvaluationContext ec) throws EvaluationException {
	//		final List<ALangObject> list = new ArrayList<ALangObject>(childArray.length);
	//		for (final Node n : childArray) list.add(n.evaluate(ec));
	//		return ArrayLangObject.create(list);
	//	}

	@Override
	public <R, T> R jjtAccept(final IFormExpressionParserVisitor<R, T> visitor, final T data) throws EvaluationException {
		return visitor.visit(this, data);
	}

	public void init() throws ParseException {
		//		childArray = getChildArray();
	}
}