package de.xima.fc.form.expression.impl.function;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;
import de.xima.fc.form.expression.object.BooleanLangObject;
import de.xima.fc.form.expression.object.NumberLangObject;

public enum EAttrAccessorBoolean implements IFunction<BooleanLangObject> {
	/**
	 * @return {@link NumberLangObject}. <code>0</code>, when this is false, <code>1</code> when this is true.
	 */
	to_number() {
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final BooleanLangObject thisContext, final ALangObject... args)
				throws EvaluationException {
			return thisContext.booleanValue() ? NumberLangObject.getOneInstance() : NumberLangObject.getZeroInstance();
		}
	}
	;

	private final String[] argList;
	private EAttrAccessorBoolean(final String... argList) {
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
		return Type.BOOLEAN;
	}

	@Override
	public Node getNode() {
		return null;
	}

	@Override
	public abstract ALangObject evaluate(final IEvaluationContext ec, final BooleanLangObject thisContext,
			final ALangObject... args) throws EvaluationException;
}
