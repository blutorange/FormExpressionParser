package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.util.EFunctionType;
import de.xima.fc.form.expression.visitor.IFormExpressionParserVisitor;

public class ASTParenthesesFunction extends AFunctionCallNode {
	private String name;

	public ASTParenthesesFunction(final int id) {
		super(id, ChainType.INSTANCE_METHOD);
	}

	public ASTParenthesesFunction(final FormExpressionParser p, final int id) {
		super(p, id, ChainType.INSTANCE_METHOD);
	}

	//TODO remove me
	//	private ALangObject[] getEvaluatedArgsArray(final IEvaluationContext ec) throws EvaluationException {
	//		final Node[] args = getChildArray();
	//		final ALangObject[] evaluatedArgs = new ALangObject[args.length];
	//		for (int i = 0; i != args.length; ++i)
	//			evaluatedArgs[i] = args[i].evaluate(ec);
	//		return evaluatedArgs;
	//	}

	//TODO remove this
	//	@Override
	//	public ALangObject evaluate(final IEvaluationContext ec) throws EvaluationException {
	//		final INamedFunction<NullLangObject> function = ec.getNamespace().globalMethod(name);
	//		if (function == null) throw new NoSuchFunctionException("global function", name, ec);
	//		final ALangObject[] eval = getEvaluatedArgsArray(ec);
	//		return ALangObject.create(function.evaluate(ec, NullLangObject.getInstance(), eval));
	//	}

	//	@Override
	//	public ALangObject chain(final ALangObject thisContext, final IEvaluationContext ec) throws EvaluationException {
	//		final ALangObject[] eval = getEvaluatedArgsArray(ec);
	//		return thisContext.evaluateInstanceMethod(name, ec, eval);
	//	}

	@Override
	public void init(final String name) throws ParseException {
		// Set name.
		if (name == null) throw new ParseException("Name is null");
		this.name = name;
	}

	@Override
	public String toString() {
		return "ParenthesesFunction(" + String.valueOf(name) + ")";
	}

	@Override
	public EFunctionType getType() {
		return EFunctionType.PARENTHESES;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public <R, T> R jjtAccept(final IFormExpressionParserVisitor<R, T> visitor, final T data) throws EvaluationException {
		return visitor.visit(this, data);
	}

}