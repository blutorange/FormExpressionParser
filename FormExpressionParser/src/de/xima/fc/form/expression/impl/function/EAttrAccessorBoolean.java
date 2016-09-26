package de.xima.fc.form.expression.impl.function;

import org.apache.commons.lang3.NotImplementedException;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.IFunction;
import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.BooleanLangObject;

public enum EAttrAccessorBoolean implements IFunction<BooleanLangObject> {
	;

	@Override
	public String getName() {
		return toString();
	}

	@Override
	public ALangObject evaluate(final IEvaluationContext ec, final BooleanLangObject thisContext,
			final ALangObject... args) throws EvaluationException {
		throw new NotImplementedException("cannot happen");
	}
}
