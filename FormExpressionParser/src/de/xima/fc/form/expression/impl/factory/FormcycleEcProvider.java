package de.xima.fc.form.expression.impl.factory;

import de.xima.fc.form.expression.exception.CannotAcquireEvaluationContextException;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.parse.IEvaluationContextProvider;
import de.xima.fc.form.expression.impl.externalcontext.FormcycleExternalContext;

public enum FormcycleEcProvider implements IEvaluationContextProvider<FormcycleExternalContext> {
	INSTANCE;
	@Override
	public IEvaluationContext getContextWithExternal(final FormcycleExternalContext ex) throws CannotAcquireEvaluationContextException {
		final IEvaluationContext ec;
		try {
			ec = FormcycleEcFactory.getFactoryInstance().create();
		}
		catch (final Exception e) {
			throw new CannotAcquireEvaluationContextException(e);
		}
		ec.setExternalContext(ex);
		return ec;
	}

	@Override
	public void getProvidedScopeInfo() {
		// TODO Auto-generated method stub
		throw new RuntimeException("TODO - not yet implemented");
	}

}
