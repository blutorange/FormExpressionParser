package de.xima.fc.form.expression.impl.externalcontext;

import javax.annotation.Nonnull;

import de.xima.fc.form.expression.iface.evaluate.IEvaluationContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContext;
import de.xima.fc.form.expression.iface.evaluate.IExternalContextCommand;

public class DummyExternalContext extends AGenericExternalContext implements IExternalContext {

	private static class InstanceHolder {
		@Nonnull
		public final static DummyExternalContext INSTANCE = new DummyExternalContext();
	}

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
	public static AGenericExternalContext getInstance() {
		return InstanceHolder.INSTANCE;
	}
}