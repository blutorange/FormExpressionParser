package de.xima.fc.form.expression.iface.parse;

import de.xima.fc.form.expression.exception.CannotAcquireEvaluationContextException;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.context.IExternalContext;

public interface IEvaluationContextProvider<T extends IExternalContext> {
	public IEvaluationContext getContextWithExternal(T ex) throws CannotAcquireEvaluationContextException;
	public void getProvidedScopeInfo();
}
