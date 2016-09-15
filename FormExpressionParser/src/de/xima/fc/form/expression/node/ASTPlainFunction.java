package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.error.EvaluationException;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.util.EFunctionType;

public
@SuppressWarnings("all")
class ASTPlainFunction extends FunctionCallNode {

	private String name;

	private final ALangObject[] emptyArgsArray = new ALangObject[0];

	public ASTPlainFunction(final int id) {
		super(id);
	}

	public ASTPlainFunction(final FormExpressionParser p, final int id) {
		super(p, id);
	}

	public String getName() {
		return name;
	}

	@Override
	public ALangObject evaluate(final IEvaluationContext ec) throws EvaluationException {
		return ALangObject.create(ec.getBinding().getVariable(name));
	}

	@Override
	public ALangObject chain(final ALangObject thisContext, final IEvaluationContext ec) throws EvaluationException {
		return thisContext.evaluateAttrAccessor(name, ec);
	}

	@Override
	public void init(final String name) throws ParseException {
		if (name == null) throw new ParseException("name is null");
		this.name =  name;
	}

	@Override
	public EFunctionType getType() {
		return EFunctionType.PLAIN;
	}

	@Override
	public String toString() {
		return "PlainFunction(" + name + ")";
	}
}