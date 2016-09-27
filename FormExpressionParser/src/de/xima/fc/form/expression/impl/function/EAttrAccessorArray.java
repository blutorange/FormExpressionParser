package de.xima.fc.form.expression.impl.function;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;
import de.xima.fc.form.expression.object.ArrayLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;

public enum EAttrAccessorArray implements IFunction<ArrayLangObject> {
	/**
	 * @return {@link NumberLangObject}. The number of entries in this array, >=0.
	 */
	length() {
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext, final ALangObject... args)
				throws EvaluationException {
			return NumberLangObject.create(thisContext.length());
		}
	}
	;

	private final String[] argList;
	private EAttrAccessorArray(final String... argList) {
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
		return Type.ARRAY;
	}

	@Override
	public Node getNode() {
		return null;
	}

	@Override
	public abstract ALangObject evaluate(final IEvaluationContext ec, final ArrayLangObject thisContext,
			final ALangObject... args) throws EvaluationException;
}
