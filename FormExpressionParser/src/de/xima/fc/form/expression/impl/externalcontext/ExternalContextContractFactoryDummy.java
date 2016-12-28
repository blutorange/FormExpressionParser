package de.xima.fc.form.expression.impl.externalcontext;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContextCommand;
import de.xima.fc.form.expression.iface.factory.IExternalContextContractFactory;

@ParametersAreNonnullByDefault
public final class ExternalContextContractFactoryDummy<T> extends AGenericExternalContextFactory<T> {
	private static final long serialVersionUID = 1L;

	protected ExternalContextContractFactoryDummy() {}

	@Override
	public AGenericExternalContext make(final T object) {
		return InstanceHolder.CONTEXT;
	}

	// safe as type parameter / value is never used by this dummy implementation
	@SuppressWarnings("unchecked")
	public static <T> IExternalContextContractFactory<T> getInstance() {
		return (IExternalContextContractFactory<T>)InstanceHolder.FACTORY;
	}

	private final static class DummyImpl extends AGenericExternalContext implements IExternalContext {
		protected DummyImpl() {}
		@Override
		public void finishWriting() {
		}
		@Override
		public void write(final String data) {
		}
		@Override
		public void process(final IExternalContextCommand command, final IEvaluationContext ec) {
		}
		@Override
		public void beginWriting() {
		}
		@Override
		public void reset() {
		}
	}

	private static class InstanceHolder {
		public final static DummyImpl CONTEXT = new DummyImpl();
		public final static IExternalContextContractFactory<Object> FACTORY = new ExternalContextContractFactoryDummy<>();
	}
}