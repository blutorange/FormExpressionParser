/* Generated By:JJTree: Do not edit this line. ASTParenthesesFunction.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.xima.fc.form.expression.node;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.INamedFunction;
import de.xima.fc.form.expression.error.EvaluationException;
import de.xima.fc.form.expression.error.NoSuchFunctionException;
import de.xima.fc.form.expression.grammar.FormExpressionParser;
import de.xima.fc.form.expression.grammar.ParseException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.NullLangObject;
import de.xima.fc.form.expression.util.EFunctionType;

public
@SuppressWarnings("all")
class ASTParenthesesFunction extends FunctionCallNode {
	private String name;
	private MySimpleNode[] args;

	public ASTParenthesesFunction(final int id) {
		super(id);
	}

	public ASTParenthesesFunction(final FormExpressionParser p, final int id) {
		super(p, id);
	}

	private ALangObject[] getEvaluatedArgsArray(final IEvaluationContext ec) throws EvaluationException {
		final ALangObject[] evaluatedArgs = new ALangObject[args.length];
		for (int i = 0; i != args.length; ++i)
			evaluatedArgs[i] = args[i].evaluate(ec);
		return evaluatedArgs;
	}

	@Override
	public ALangObject evaluate(final IEvaluationContext ec) throws EvaluationException {
		final INamedFunction<NullLangObject> function = ec.getNamespace().globalMethod(name);
		if (function == null) throw new NoSuchFunctionException(name, ec);
		final ALangObject[] eval = getEvaluatedArgsArray(ec);
		return function.evaluate(ec, NullLangObject.getInstance(), eval);
	}

	@Override
	public ALangObject chain(final ALangObject thisContext, final IEvaluationContext ec) throws EvaluationException {
		final ALangObject[] eval = getEvaluatedArgsArray(ec);
		return thisContext.evaluateInstanceMethod(name, ec, eval);
	}

	@Override
	public void init(final String name) throws ParseException {
		// Set arguments.
		args = getChildArray();

		// Set name.
		if (name == null) throw new ParseException("name is null");
		this.name = name;
	}

	@Override
	public EFunctionType getType() {
		return EFunctionType.PARENTHESES;
	}
}
/* JavaCC - OriginalChecksum=aeeab3374d7dbccda65ed7f40d18fef4 (do not edit this line) */
