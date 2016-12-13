package de.xima.fc.form.expression.impl.externalcontext;

import javax.annotation.ParametersAreNonnullByDefault;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContextCommand;
import de.xima.fc.form.expression.iface.evaluate.IExternalContextContractFactory;

@ParametersAreNonnullByDefault
public final class DummyExternalContextFactory<T> extends AGenericExternalContextFactory<T> {
	private static final long serialVersionUID = 1L;

	private DummyExternalContextFactory() {}

	@Override
	public AGenericExternalContext makeExternalContext(final T object) {
		return InstanceHolder.CONTEXT;
	}

	// safe as type parameter / value is never used by this dummy implementation
	@SuppressWarnings("unchecked")
	public static <T> IExternalContextContractFactory<T> getFactory() {
		return (IExternalContextContractFactory<T>)InstanceHolder.FACTORY;
	}

	private static class DummyImpl extends AGenericExternalContext implements IExternalContext {
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
	}

	private static class InstanceHolder {
		public final static DummyImpl CONTEXT = new DummyImpl();
		public final static IExternalContextContractFactory<Object> FACTORY = new DummyExternalContextFactory<>();
	}
}