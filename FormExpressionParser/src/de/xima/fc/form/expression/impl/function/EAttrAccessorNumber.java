package de.xima.fc.form.expression.impl.function;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;
import de.xima.fc.form.expression.object.NumberLangObject;

public enum EAttrAccessorNumber implements IFunction<NumberLangObject> {
	/**
	 * @return {@link NumberLangObject}. The sine of this number.
	 */
	sin() {
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext, final ALangObject... args)
				throws EvaluationException {
			return thisContext.sin();
		}
	}
	;

	private final String[] argList;
	private EAttrAccessorNumber(final String... argList) {
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
		return Type.NUMBER;
	}

	@Override
	public Node getNode() {
		return null;
	}

	@Override
	public abstract ALangObject evaluate(final IEvaluationContext ec, final NumberLangObject thisContext,
			final ALangObject... args) throws EvaluationException;

}
