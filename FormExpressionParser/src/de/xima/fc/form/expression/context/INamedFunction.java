package de.xima.fc.form.expression.context;

import org.jetbrains.annotations.Nullable;

import de.xima.fc.form.expression.exception.EvaluationException;
import de.xima.fc.form.expression.object.ALangObject;

public interface INamedFunction<T extends ALangObject> {
	@Nullable
	public abstract ALangObject evaluate(IEvaluationContext ec, T thisContext, ALangObject... args) throws EvaluationException;
	public String getName();
}
