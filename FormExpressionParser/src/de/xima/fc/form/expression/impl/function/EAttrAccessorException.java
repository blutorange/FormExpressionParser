package de.xima.fc.form.expression.impl.function;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.grammar.Node;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.ALangObject.Type;
import de.xima.fc.form.expression.object.ExceptionLangObject;
import de.xima.fc.form.expression.object.StringLangObject;

public enum EAttrAccessorException implements IFunction<ExceptionLangObject> {
	/**
	 * @return {@link StringLangObject}. The message for this exception. The empty string when this exception does not contain a message.
	 */
	message() {
		@Override
		public ALangObject evaluate(final IEvaluationContext ec, final ExceptionLangObject thisContext, final ALangObject... args)
				throws EvaluationException {
			return StringLangObject.create(thisContext.exceptionValue().getMessage());
		}
	}
	;

	private final String[] argList;
	private EAttrAccessorException(final String... argList) {
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
		return Type.EXCEPTION;
	}

	@Override
	public Node getNode() {
		return null;
	}

	@Override
	public abstract ALangObject evaluate(final IEvaluationContext ec, final ExceptionLangObject thisContext,
			final ALangObject... args) throws EvaluationException;
}
