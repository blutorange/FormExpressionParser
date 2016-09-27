package de.xima.fc.form.expression.impl.function;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;
import de.xima.fc.form.expression.object.FunctionLangObject;
import de.xima.fc.form.expression.object.StringLangObject;

public enum EAttrAccessorFunction implements IFunction<FunctionLangObject> {
	/**
	 * @return {@link StringLangObject}. The declared name of this function. The empty string when an anonymous function.
	 */
	name() {
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final FunctionLangObject thisContext, final ALangObject... args)
				throws EvaluationException {
			return StringLangObject.create(thisContext.functionValue().getDeclaredName());
		}
	}
	;

	private final String[] argList;
	private EAttrAccessorFunction(final String... argList) {
		this.argList = argList;
	}


	@Override
	public String getDeclaredName() {
		return toString();
	}

	@Override
	public String[] getDeclaredArgumentList() {
		return argList;
	}

	@Override
	public Type getThisContextType() {
		return Type.FUNCTION;
	}

	@Override
	public Node getNode() {
		return null;
	}

	@Override
	public abstract ALangObject evaluate(final IEvaluationContext ec, final FunctionLangObject thisContext,
			final ALangObject... args) throws EvaluationException;
}
