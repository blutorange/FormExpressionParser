package de.xima.fc.form.expression.impl.embedment;

import de.xima.fc.form.expression.context.IEvaluationContext;
import de.xima.fc.form.expression.exception.EmbedmentOutputException;

public interface IEmbedmentHandler<T> {
	public void beginEmbedment();
	public void endEmbedment();
	public void output(String data, T object, IEvaluationContext ec) throws EmbedmentOutputException;
}
