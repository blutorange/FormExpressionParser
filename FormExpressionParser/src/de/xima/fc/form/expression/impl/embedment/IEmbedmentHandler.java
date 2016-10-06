package de.xima.fc.form.expression.impl.embedment;

import de.xima.fc.form.expression.context.IEvaluationContext;

public interface IEmbedmentHandler {
	public void beginEmbedment(IEvaluationContext ec);
	public void endEmbedment(IEvaluationContext ec);
	public boolean isDoOutput();
}