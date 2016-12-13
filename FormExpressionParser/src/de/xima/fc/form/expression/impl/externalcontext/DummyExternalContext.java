package de.xima.fc.form.expression.impl.externalcontext;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContextCommand;
import de.xima.fc.form.expression.iface.evaluate.IExternalContextContractFactory;
import de.xima.fc.form.expression.util.Void;

public class DummyExternalContext extends AGenericExternalContext implements IExternalContext {

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

	@Nonnull
	public static IExternalContextContractFactory<Void> getFactory() {
		return InstanceHolder.FACTORY;
	}

	private static class InstanceHolder {
		@Nonnull
		public final static DummyExternalContext CONTEXT = new DummyExternalContext();
		@Nonnull
		public final static IExternalContextContractFactory<Void> FACTORY = new DummyExternalContextFactory();
	}

	private static class DummyExternalContextFactory extends AGenericExternalContextFactory<Void> {
		private static final long serialVersionUID = 1L;

		@Override
		public AGenericExternalContext makeExternalContext(final de.xima.fc.form.expression.util.Void object) {
			return InstanceHolder.CONTEXT;
		}
	}
}