package de.xima.fc.form.expression.context;

import de.xima.fc.form.expression.error.EvaluationException;
import de.xima.fc.form.expression.object.ALangObject;

public interface INamedFunction<T extends ALangObject> {
	public abstract ALangObject evaluate(IEvaluationContext ec, T thisContext, ALangObject... args) throws EvaluationException;
	public String getName();
}
