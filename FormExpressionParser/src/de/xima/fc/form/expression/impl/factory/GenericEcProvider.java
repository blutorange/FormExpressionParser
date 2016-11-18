package de.xima.fc.form.expression.impl.factory;

import de.xima.fc.form.expression.exception.CannotAcquireEvaluationContextException;
import de.xima.fc.form.expression.iface.context.IEvaluationContext;
import de.xima.fc.form.expression.iface.parse.IEvaluationContextProvider;
import de.xima.fc.form.expression.impl.externalcontext.AGenericExternalContext;

public enum GenericEcProvider implements IEvaluationContextProvider<AGenericExternalContext> {
	INSTANCE;
	@Override
	public IEvaluationContext getContextWithExternal(final AGenericExternalContext ex)
			throws CannotAcquireEvaluationContextException {
		final IEvaluationContext ec;
		try {
			ec = GenericEcFactory.getFactoryInstance().create();
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
