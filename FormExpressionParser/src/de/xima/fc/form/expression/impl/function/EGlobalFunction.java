package de.xima.fc.form.expression.impl.function;

import org.apache.commons.lang3.NotImplementedException;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.context.INamedFunction;
import de.xima.fc.form.expression.error.EvaluationException;
import de.xima.fc.form.expression.object.ALangObject;
import de.xima.fc.form.expression.object.NullLangObject;

public enum EGlobalFunction implements INamedFunction<NullLangObject> {

	;

	@Override
	public String getName() {
		return toString();
	}

	@Override
	public ALangObject evaluate(final IEvaluationContext ec, final NullLangObject thisContext,
			final ALangObject... args) throws EvaluationException {
		throw new NotImplementedException("cannot happen");
	}
}
